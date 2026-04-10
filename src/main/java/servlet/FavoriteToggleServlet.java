package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.FavoriteDAO;
import dao.FavoriteDAOImpl;
import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Favorite;
import entity.Series;
import entity.User;
import utils.AuthUtil;

@WebServlet("/favorite/toggle")
public class FavoriteToggleServlet extends HttpServlet {

    private final FavoriteDAO favoriteDao = new FavoriteDAOImpl();
    private final SeriesDAO seriesDao = new SeriesDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        User user = AuthUtil.get(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String seriesIdParam = req.getParameter("seriesId");
        if (seriesIdParam == null || seriesIdParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Long seriesId = Long.parseLong(seriesIdParam);
        Series series = seriesDao.findById(seriesId);

        if (series != null) {
            boolean isFav = favoriteDao.isFavorite(user.getId(), seriesId);
            if (isFav) {
                favoriteDao.deleteByUserAndSeries(user.getId(), seriesId);
            } else {
                Favorite fav = new Favorite();
                fav.setUser(user);
                fav.setSeries(series);
                favoriteDao.create(fav);
            }
        }

        // Redirect back to the watch page
        resp.sendRedirect(req.getContextPath() + "/watch?id=" + seriesId);
    }
}