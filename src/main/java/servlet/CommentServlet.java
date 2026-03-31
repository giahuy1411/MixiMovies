package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.CommentDAO;
import dao.CommentDAOImpl;
import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Comment;
import entity.User;
import entity.Series;

/**
 * Xử lý thêm bình luận mới.
 * POST /addComment  { seriesId, content }
 */
@WebServlet("/addComment")
public class CommentServlet extends HttpServlet {

    private final CommentDAO commentDao = new CommentDAOImpl();
    private final SeriesDAO  seriesDao  = new SeriesDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Kiểm tra đăng nhập bằng AuthUtil
        User user = utils.AuthUtil.get(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String seriesIdParam = req.getParameter("seriesId");
        String content       = req.getParameter("content");

        if (seriesIdParam == null || content == null || content.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Long  seriesId = Long.parseLong(seriesIdParam);
        Series series   = seriesDao.findById(seriesId);
        if (series == null) {
            resp.sendError(404);
            return;
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setSeries(series);
        comment.setContent(content.trim());
        comment.setCreatedAt(new Date());
        commentDao.create(comment);

        // Redirect lại trang xem phim
        resp.sendRedirect(req.getContextPath() + "/watch?id=" + seriesId);
    }
}
