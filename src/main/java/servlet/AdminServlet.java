package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.VideoDAO;
import dao.VideoDAOImpl;
import entity.Video;
import utils.OMDbClient;

@WebServlet({
    "/admin/video",
    "/admin/video/create",
    "/admin/video/update",
    "/admin/video/delete"
})
public class AdminServlet extends HttpServlet {

    private VideoDAO videoDao = new VideoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.endsWith("/admin/video")) {
            List<Video> list = videoDao.findAll();
            req.setAttribute("videos", list);
            req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
            return;
        }

        if (uri.contains("/admin/video/delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            videoDao.delete(id);
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
            String imdbId = req.getParameter("imdbId");
            if (imdbId == null || imdbId.trim().isEmpty()) {
                req.setAttribute("error", "IMDb ID không được để trống");
                req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
                return;
            }

            Video existing = videoDao.findByImdbId(imdbId);
            if (existing != null) {
                req.setAttribute("error", "Phim đã tồn tại với IMDb ID: " + imdbId);
                req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
                return;
            }

            try {
                Video video = OMDbClient.fetchMovieByImdbId(imdbId);
                videoDao.create(video);
                resp.sendRedirect(req.getContextPath() + "/admin/video");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi lấy thông tin phim: " + e.getMessage());
                req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
            }
            return;
        }

        if (uri.contains("/admin/video/update")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Video v = videoDao.findById(id);
            if (v != null) {
                v.setTitle(req.getParameter("title"));
                v.setDescription(req.getParameter("description"));
                v.setPoster(req.getParameter("poster"));
                v.setYear(Integer.parseInt(req.getParameter("year")));
                v.setDirector(req.getParameter("director"));
                v.setActors(req.getParameter("actors"));
                v.setGenre(req.getParameter("genre"));
                v.setImdbRating(Double.parseDouble(req.getParameter("imdbRating")));
                // Không set videoUrl nữa
                videoDao.update(v);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/video");
        }
    }
}