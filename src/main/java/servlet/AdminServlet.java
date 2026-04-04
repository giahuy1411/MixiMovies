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
import entity.Series;
import entity.User;
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
    private final dao.CategoryDAO categoryDAO = new dao.CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        String tab = req.getParameter("tab");
        if (tab == null) tab = "video";

        if (uri.endsWith("/admin/video") || uri.endsWith("/admin/users") || uri.endsWith("/admin/categories")) {
            // Pagination & Sorting Params
            int page = 1;
            int size = 10;
            String sortBy = req.getParameter("sortBy");
            String sortDir = req.getParameter("sortDir");
            if (req.getParameter("p") != null) page = Integer.parseInt(req.getParameter("p"));
            if (sortBy == null) sortBy = "createdAt";
            if (sortDir == null) sortDir = "desc";

            List<Series> seriesList = seriesDao.findAll(page, size, sortBy, sortDir);
            List<User> userList = userDAO.findAll();
            long totalVideos = seriesDao.count();
            long totalUsers = userList.size();
            
            // Analytics: Views by Genre
            List<Object[]> genreStats = seriesDao.getViewsByGenre();
            StringBuilder labels = new StringBuilder("[");
            StringBuilder data = new StringBuilder("[");
            for (int i = 0; i < genreStats.size(); i++) {
                Object[] row = genreStats.get(i);
                String genre = (row[0] == null) ? "Chưa phân loại" : row[0].toString();
                long views = (row[1] == null) ? 0 : (Long) row[1];
                
                labels.append("\"").append(genre).append("\"");
                data.append(views);
                
                if (i < genreStats.size() - 1) {
                    labels.append(",");
                    data.append(",");
                }
            }
            labels.append("]");
            data.append("]");

            List<entity.Category> categoryList = categoryDAO.findAll();

            req.setAttribute("seriesList", seriesList);
            req.setAttribute("userList", userList);
            req.setAttribute("categoryList", categoryList);
            req.setAttribute("totalVideos", totalVideos);
            req.setAttribute("totalUsers", totalUsers);
            req.setAttribute("totalPages", (int) Math.ceil((double) totalVideos / size));
            req.setAttribute("currentPage", page);
            req.setAttribute("sortBy", sortBy);
            req.setAttribute("sortDir", sortDir);
            req.setAttribute("chartLabels", labels.toString());
            req.setAttribute("chartData", data.toString());
            req.setAttribute("currentTab", tab);

            req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
            return;
        }

        if (uri.contains("/admin/video/delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            seriesDao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/video");
            return;
        }

        if (uri.contains("/admin/users/role")) {
            String id = req.getParameter("id");
            User u = userDAO.findById(id);
            if (u != null) {
                u.setAdmin(!u.getAdmin());
                userDAO.update(u);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/users?tab=users");
            return;
        }

        if (uri.contains("/admin/categories/delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            categoryDAO.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/categories?tab=category");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.contains("/admin/video/create")) {
            String slug = req.getParameter("slug");
            if (slug == null || slug.trim().isEmpty()) {
                req.setAttribute("error", "Slug phim không được để trống");
                req.setAttribute("seriesList", seriesDao.findAll());
                req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
                return;
            }

            Series existing = seriesDao.findBySlug(slug);
            if (existing != null) {
                req.setAttribute("error", "Phim đã tồn tại với Slug: " + slug);
                req.setAttribute("seriesList", seriesDao.findAll());
                req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
                return;
            }

            try {
                Series series = KKPhimClient.fetchSeriesBySlug(slug.trim());
                seriesDao.create(series);
                resp.sendRedirect(req.getContextPath() + "/admin/video");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi: " + e.getMessage());
                req.setAttribute("seriesList", seriesDao.findAll());
                req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
            }
            return;
        }

        if (uri.contains("/admin/video/update")) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                Long id = Long.parseLong(idStr);
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
            }
            resp.sendRedirect(req.getContextPath() + "/admin/video");
            return;
        }

        if (uri.contains("/admin/categories/create")) {
            entity.Category cat = new entity.Category();
            cat.setName(req.getParameter("name"));
            cat.setSlug(req.getParameter("slug"));
            cat.setDescription(req.getParameter("description"));
            String orderStr = req.getParameter("order");
            cat.setOrder(orderStr != null && !orderStr.isEmpty() ? Integer.parseInt(orderStr) : 0);
            cat.setActive("on".equals(req.getParameter("active")));
            categoryDAO.create(cat);
            resp.sendRedirect(req.getContextPath() + "/admin/categories?tab=category");
            return;
        }

        if (uri.contains("/admin/categories/update")) {
            Long id = Long.parseLong(req.getParameter("id"));
            entity.Category cat = categoryDAO.findById(id);
            if (cat != null) {
                cat.setName(req.getParameter("name"));
                cat.setSlug(req.getParameter("slug"));
                cat.setDescription(req.getParameter("description"));
                String orderStr = req.getParameter("order");
                cat.setOrder(orderStr != null && !orderStr.isEmpty() ? Integer.parseInt(orderStr) : 0);
                cat.setActive("on".equals(req.getParameter("active")));
                categoryDAO.update(cat);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories?tab=category");
            return;
        }
    }
}