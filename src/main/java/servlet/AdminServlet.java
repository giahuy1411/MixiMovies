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
    "/admin/users/role"
})
public class AdminServlet extends HttpServlet {

    private final SeriesDAO seriesDao = new SeriesDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        String tab = req.getParameter("tab");
        if (tab == null) tab = "video";

        if (uri.endsWith("/admin/video") || uri.endsWith("/admin/users")) {
            List<Series> seriesList = seriesDao.findAll();
            List<User> userList = userDAO.findAll();

            // Stats (Java 7 compatible)
            long totalVideos = seriesList.size();
            long totalUsers = userList.size();
            long totalViews = 0;
            long activeVideosCount = 0;
            for (Series s : seriesList) {
                if (s.getViews() != null) totalViews += s.getViews();
                if (s.getActive() != null && s.getActive()) {
                    activeVideosCount++;
                }
            }

            req.setAttribute("seriesList", seriesList);
            req.setAttribute("userList", userList);
            req.setAttribute("totalVideos", totalVideos);
            req.setAttribute("totalUsers", totalUsers);
            req.setAttribute("totalViews", totalViews);
            req.setAttribute("activeVideos", activeVideosCount);
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
                    s.setActive("on".equals(req.getParameter("active")));
                    seriesDao.update(s);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin/video");
            return;
        }
    }
}