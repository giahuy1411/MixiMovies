package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.FavoriteDAO;
import dao.FavoriteDAOImpl;
import entity.Series;
import entity.User;
import utils.AuthUtil;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final FavoriteDAO favoriteDao = new FavoriteDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        User user = AuthUtil.get(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy danh sách phim yêu thích của người dùng để hiện ở dưới trang Profile
        List<Series> favoriteSeries = favoriteDao.findSeriesByUser(user.getId());
        req.setAttribute("favoriteSeries", favoriteSeries);
        
        req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
    }
}
