package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.CommentDAO;
import dao.CommentDAOImpl;
import dao.VideoDAO;
import dao.VideoDAOImpl;
import entity.Comment;
import entity.Video;

@WebServlet("/watch")
public class DetailServlet extends HttpServlet {

    private final VideoDAO   videoDao   = new VideoDAOImpl();
    private final CommentDAO commentDao = new CommentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Video video = videoDao.findById(id);
        if (video == null) {
            resp.sendError(404);
            return;
        }

        // Tăng lượt xem
        videoDao.increaseView(id);
        // Lấy lại để views được cập nhật
        video = videoDao.findById(id);

        // Load danh sách bình luận
        List<Comment> comments = commentDao.findByVideo(id);

        req.setAttribute("video", video);
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/views/detail.jsp").forward(req, resp);
    }
}