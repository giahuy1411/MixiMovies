package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Series;
import entity.User;
import utils.AuthUtil;
import utils.Mailer;

@WebServlet("/share")
public class ShareServlet extends HttpServlet {

    private final SeriesDAO seriesDao = new SeriesDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        User user = AuthUtil.get(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String seriesIdParam = req.getParameter("seriesId");
        String emailTo = req.getParameter("email");
        String customMessage = req.getParameter("message");

        if (seriesIdParam == null || seriesIdParam.isEmpty() || emailTo == null || emailTo.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Validate email format
        if (!emailTo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            req.getSession().setAttribute("shareError", "Email không hợp lệ");
            resp.sendRedirect(req.getContextPath() + "/watch?id=" + seriesIdParam);
            return;
        }

        // Validate message length
        if (customMessage != null && customMessage.length() > 500) {
            req.getSession().setAttribute("shareError", "Lời nhắn quá dài (tối đa 500 ký tự)");
            resp.sendRedirect(req.getContextPath() + "/watch?id=" + seriesIdParam);
            return;
        }

        Long seriesId;
        try {
            seriesId = Long.parseLong(seriesIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        Series series = seriesDao.findById(seriesId);

        if (series != null) {
            String scheme = req.getScheme();
            String serverName = req.getServerName();
            int serverPort = req.getServerPort();
            String contextPath = req.getContextPath();
            
            String baseUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;
            String subject = "Bạn bè chia sẻ phim hay: " + series.getTitle();
            String link = baseUrl + "/watch?id=" + series.getId();
            
            StringBuilder body = new StringBuilder();
            body.append("<h3>Chào bạn!</h3>");
            body.append("<p>Người bạn <b>").append(utils.ParamUtil.htmlEscape(user.getFullname())).append("</b> đã chia sẻ bộ phim <b>")
                .append(utils.ParamUtil.htmlEscape(series.getTitle())).append("</b> cho bạn tại MixiMovies.</p>");
            
            if (customMessage != null && !customMessage.trim().isEmpty()) {
                body.append("<p><i>Lời nhắn:</i> ").append(utils.ParamUtil.htmlEscape(customMessage)).append("</p>");
            }
            
            body.append("<br><a href='").append(link).append("' style='padding:10px 20px; background:#e50914; color:#fff; text-decoration:none; border-radius:5px;'>Xem phim ngay</a>");
            
            try {
                Mailer.send(user.getEmail(), emailTo, subject, body.toString());
                req.getSession().setAttribute("shareSuccess", "Đã gửi Email chia sẻ phim thành công!");
            } catch (Exception e) {
                req.getSession().setAttribute("shareError", "Có lỗi xảy ra khi gửi email.");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/watch?id=" + seriesId);
    }
}
