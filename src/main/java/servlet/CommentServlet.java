package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.CommentDAO;
import dao.CommentDAOImpl;
import dao.VideoDAO;
import dao.VideoDAOImpl;
import entity.Comment;
import entity.User;
import entity.Video;

/**
 * Xử lý thêm bình luận mới.
 * POST /addComment  { videoId, content }
 */
@WebServlet("/addComment")
public class CommentServlet extends HttpServlet {

    private final CommentDAO commentDao = new CommentDAOImpl();
    private final VideoDAO   videoDao   = new VideoDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Kiểm tra đăng nhập
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String videoIdParam = req.getParameter("videoId");
        String content      = req.getParameter("content");

        if (videoIdParam == null || content == null || content.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Long  videoId = Long.parseLong(videoIdParam);
        Video video   = videoDao.findById(videoId);
        if (video == null) {
            resp.sendError(404);
            return;
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setVideo(video);
        comment.setContent(content.trim());
        comment.setCreatedAt(new Date());
        commentDao.create(comment);

        // Redirect lại trang xem phim
        resp.sendRedirect(req.getContextPath() + "/watch?id=" + videoId);
    }
}
