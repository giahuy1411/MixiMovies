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
import utils.ParamUtil;

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

        seriesDao.increaseView(id);

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
            if (currentEpisode == null) {
                currentEpisode = series.getEpisodes().get(0);
            }
        }

        // Load danh sách bình luận
        List<Comment> comments = commentDao.findBySeries(id);

        // Kiểm tra trạng thái yêu thích
        boolean isFavorite = false;
        entity.User user = utils.AuthUtil.get(req);
        if (user != null) {
            dao.FavoriteDAO favDao = new dao.FavoriteDAOImpl();
            isFavorite = favDao.isFavorite(user.getId(), id);
        }

        // Kiểm tra Premium Lock: Phim < 10 ngày chỉ dành cho Premium
        boolean premiumLocked = false;
        boolean isNewMovie = false;
        if (series.getCreatedAt() != null) {
            long diffMs = System.currentTimeMillis() - series.getCreatedAt().getTime();
            long diffDays = diffMs / (1000 * 60 * 60 * 24);
            isNewMovie = diffDays < 1;

            if (isNewMovie) {
                boolean isAdmin = user != null && Boolean.TRUE.equals(user.getAdmin());
                boolean isPremium = user != null && Boolean.TRUE.equals(user.getPremium());
                premiumLocked = !isAdmin && !isPremium;
            }
        }

        req.setAttribute("series", series);
        req.setAttribute("currentEpisode", currentEpisode);
        req.setAttribute("comments", comments);
        req.setAttribute("isFavorite", isFavorite);
        req.setAttribute("premiumLocked", premiumLocked);
        req.setAttribute("isNewMovie", isNewMovie);
        req.getRequestDispatcher("/views/detail.jsp").forward(req, resp);
    }
}