package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.VideoDAO;
import dao.VideoDAOImpl;
import entity.Video;

@WebServlet("/watch")
public class DetailServlet extends HttpServlet {

    private VideoDAO videoDao = new VideoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Long id = Long.parseLong(idParam);
        Video video = videoDao.findById(id);
        if (video == null) {
            resp.sendError(404);
            return;
        }

        // Tăng lượt xem
        videoDao.increaseView(id);
        // Lấy lại video sau khi tăng view (hoặc dùng video đã có nhưng views chưa tăng)
        video = videoDao.findById(id); // cập nhật views

        req.setAttribute("video", video);
        req.getRequestDispatcher("/views/detail.jsp").forward(req, resp);
    }
}