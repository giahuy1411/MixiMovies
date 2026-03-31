package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.CommentDAO;
import dao.CommentDAOImpl;
import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Comment;
import entity.Series;
import entity.Episode;

@WebServlet("/watch")
public class DetailServlet extends HttpServlet {

    private final SeriesDAO   seriesDao   = new SeriesDAOImpl();
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

        Series series = seriesDao.findById(id);
        if (series == null) {
            resp.sendError(404);
            return;
        }

        // Tăng lượt xem
        seriesDao.increaseView(id);
        // Lấy lại để views được cập nhật
        series = seriesDao.findById(id);

        // Xử lý chọn tập phim (episode)
        String epParam = req.getParameter("ep");
        Episode currentEpisode = null;
        if (series.getEpisodes() != null && !series.getEpisodes().isEmpty()) {
            if (epParam != null) {
                for (Episode ep : series.getEpisodes()) {
                    if (ep.getId().toString().equals(epParam)) {
                        currentEpisode = ep;
                        break;
                    }
                }
            }
            // Mặc định chọn tập 1 nếu không có tham số ep hoặc không tìm thấy
            if (currentEpisode == null) {
                currentEpisode = series.getEpisodes().get(0);
            }
        }

        // Load danh sách bình luận
        List<Comment> comments = commentDao.findBySeries(id);

        req.setAttribute("series", series);
        req.setAttribute("currentEpisode", currentEpisode); // Dùng cho player
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/views/detail.jsp").forward(req, resp);
    }
}