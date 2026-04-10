package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import entity.Series;
import entity.User;
import entity.Category;
import utils.KKPhimClient;

@WebServlet({
    "/admin/video",
    "/admin/video/create",
    "/admin/video/update",
    "/admin/video/delete",
    "/admin/users",
    "/admin/users/role",
    "/admin/categories",
    "/admin/categories/create",
    "/admin/categories/update",
    "/admin/categories/delete"
})
public class AdminServlet extends HttpServlet {

    private final SeriesDAO seriesDao = new SeriesDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    private static final int PAGE_SIZE = 10;

    /**
     * Nạp toàn bộ dữ liệu cần thiết cho Dashboard Admin (Video, User, Category, Thống kê).
     * Dùng chung cho cả GET và POST (khi cần quay lại trang admin với báo lỗi).
     */
    private void loadDashboardData(HttpServletRequest req) {
        String tab = req.getParameter("tab");
        if (tab == null) tab = "video";

        int page = 1;
        try {
            String pStr = req.getParameter("p");
            if (pStr != null) page = Integer.parseInt(pStr);
        } catch (NumberFormatException e) {
            page = 1;
        }

        String sortBy = req.getParameter("sortBy");
        String sortDir = req.getParameter("sortDir");
        if (sortBy == null) sortBy = "createdAt";
        if (sortDir == null) sortDir = "desc";

        List<Series> seriesList = seriesDao.findAll(page, PAGE_SIZE, sortBy, sortDir);
        List<User> userList = userDAO.findAll();
        List<Category> categoryList = categoryDAO.findAll();
        
        long totalVideos = seriesDao.count();
        long totalUsers = userList.size();

        // Thống kê Analytics
        List<Object[]> genreStats = seriesDao.getViewsByGenre();
        StringBuilder labels = new StringBuilder("[");
        StringBuilder data = new StringBuilder("[");
        for (int i = 0; i < genreStats.size(); i++) {
            Object[] row = genreStats.get(i);
            String genre = (row[0] == null) ? "Khác" : row[0].toString();
            // Sử dụng Number để tránh ClassCastException (Integer vs Long vs BigDecimal)
            long views = (row[1] == null) ? 0 : ((Number) row[1]).longValue();
            
            labels.append("\"").append(genre.replace("\"", "\\\"")).append("\"");
            data.append(views);
            if (i < genreStats.size() - 1) {
                labels.append(","); data.append(",");
            }
        }
        labels.append("]");
        data.append("]");

        req.setAttribute("seriesList", seriesList);
        req.setAttribute("userList", userList);
        req.setAttribute("categoryList", categoryList);
        req.setAttribute("totalVideos", totalVideos);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalPages", (int) Math.ceil((double) totalVideos / PAGE_SIZE));
        req.setAttribute("currentPage", page);
        req.setAttribute("sortBy", sortBy);
        req.setAttribute("sortDir", sortDir);
        req.setAttribute("chartLabels", labels.toString());
        req.setAttribute("chartData", data.toString());
        req.setAttribute("currentTab", tab);
        
        // Thống kê bổ sung
        req.setAttribute("activeVideos", seriesDao.countActive());
        req.setAttribute("totalViews", seriesDao.getTotalViews());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();

        // Chặn các thao tác xóa qua GET để bảo mật
        if (uri.contains("/delete") || uri.contains("/role")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Thao tác này yêu cầu phương thức POST để đảm bảo bảo mật.");
            return;
        }

        loadDashboardData(req);
        req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        try {
            if (uri.contains("/admin/video/create")) {
                handleCreateVideo(req, resp);
            } else if (uri.contains("/admin/video/update")) {
                handleUpdateVideo(req, resp);
            } else if (uri.contains("/admin/video/delete")) {
                handleDeleteVideo(req, resp);
            } else if (uri.contains("/admin/users/role")) {
                handleUpdateRole(req, resp);
            } else if (uri.contains("/admin/categories/create")) {
                handleCreateCategory(req, resp);
            } else if (uri.contains("/admin/categories/update")) {
                handleUpdateCategory(req, resp);
            } else if (uri.contains("/admin/categories/delete")) {
                handleDeleteCategory(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
            loadDashboardData(req);
            req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
        }
    }

    private void handleCreateVideo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String slug = req.getParameter("slug");
        if (slug == null || slug.trim().isEmpty()) {
            throw new RuntimeException("Slug phim không được để trống.");
        }
        if (seriesDao.findBySlug(slug.trim()) != null) {
            throw new RuntimeException("Phim này đã tồn tại trong hệ thống.");
        }
        Series series = KKPhimClient.fetchSeriesBySlug(slug.trim());
        seriesDao.create(series);
        resp.sendRedirect(req.getContextPath() + "/admin/video");
    }

    private void handleUpdateVideo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long id = Long.parseLong(req.getParameter("id"));
        Series s = seriesDao.findById(id);
        if (s != null) {
            s.setTitle(req.getParameter("title"));
            s.setDescription(req.getParameter("description"));
            s.setPoster(req.getParameter("poster"));
            s.setYear(Integer.parseInt(req.getParameter("year")));
            s.setDirector(req.getParameter("director"));
            s.setActors(req.getParameter("actors"));
            s.setGenre(req.getParameter("genre"));
            String catId = req.getParameter("categoryId");
            if (catId != null && !catId.isEmpty()) {
                s.setCategory(categoryDAO.findById(Long.parseLong(catId)));
            }
            s.setActive("on".equals(req.getParameter("active")));
            seriesDao.update(s);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/video");
    }

    private void handleDeleteVideo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long id = Long.parseLong(req.getParameter("id"));
        seriesDao.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/video");
    }

    private void handleUpdateRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter("id");
        User u = userDAO.findById(id);
        if (u != null) {
            u.setAdmin(!u.getAdmin());
            userDAO.update(u);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users?tab=users");
    }

    private void handleCreateCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Category cat = new Category();
        cat.setName(req.getParameter("name"));
        cat.setSlug(req.getParameter("slug"));
        cat.setDescription(req.getParameter("description"));
        cat.setOrder(Integer.parseInt(req.getParameter("order")));
        cat.setActive("on".equals(req.getParameter("active")));
        categoryDAO.create(cat);
        resp.sendRedirect(req.getContextPath() + "/admin/categories?tab=category");
    }

    private void handleUpdateCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long id = Long.parseLong(req.getParameter("id"));
        Category cat = categoryDAO.findById(id);
        if (cat != null) {
            cat.setName(req.getParameter("name"));
            cat.setSlug(req.getParameter("slug"));
            cat.setDescription(req.getParameter("description"));
            cat.setOrder(Integer.parseInt(req.getParameter("order")));
            cat.setActive("on".equals(req.getParameter("active")));
            categoryDAO.update(cat);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/categories?tab=category");
    }

    private void handleDeleteCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long id = Long.parseLong(req.getParameter("id"));
        categoryDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/categories?tab=category");
    }
}