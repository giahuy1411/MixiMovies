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
import entity.Series;
import utils.KKPhimClient;

@WebServlet({
    "/admin/video",
    "/admin/video/create",
    "/admin/video/update",
    "/admin/video/delete"
})
public class AdminServlet extends HttpServlet {

    private final SeriesDAO seriesDao = new SeriesDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.endsWith("/admin/video")) {
            List<Series> list = seriesDao.findAll();
            req.setAttribute("seriesList", list);
            req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
            return;
        }

        if (uri.contains("/admin/video/delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            seriesDao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/video");
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
                // Không update episodes ở đây vì phức tạp, admin chỉ quản lý metadata cơ bản ở form sửa
                seriesDao.update(s);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/video");
        }
    }
}