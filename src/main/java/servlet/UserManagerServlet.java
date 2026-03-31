package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import utils.Mailer;

@WebServlet({ "/admin/users/toggle" })
public class UserManagerServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String id = req.getParameter("id");
        String action = req.getParameter("action"); // "lock" hoặc "unlock"
        String reason = req.getParameter("reason");

        User user = userDAO.findById(id);
        if (user != null) {
            boolean active = "unlock".equals(action);
            userDAO.setActive(id, active);

            // Gửi email thông báo
            try {
                String subject = active ? "🔓 Tài khoản của bạn đã được mở khóa" : "🔒 Tài khoản của bạn đã bị khóa";
                StringBuilder body = new StringBuilder();
                body.append("Xin chào ").append(user.getFullname()).append(",\n\n");
                
                if (active) {
                    body.append("Chúng tôi vui lòng thông báo rằng tài khoản của bạn đã được mở khóa. Bây giờ bạn có thể đăng nhập lại vào MixiMovies.\n");
                } else {
                    body.append("Tài khoản của bạn đã bị tạm khóa bởi Quản trị viên.\n");
                    if (reason != null && !reason.trim().isEmpty()) {
                        body.append("Lý do: ").append(reason).append("\n");
                    }
                }
                
                body.append("\nTrân trọng,\nMixiMovies Team");

                Mailer.send(
                    "miximovies.support@gmail.com",
                    user.getEmail(),
                    subject,
                    body.toString()
                );
            } catch (Exception e) {
                System.out.println("⚠️ Gửi mail thông báo trạng thái tài khoản thất bại: " + e.getMessage());
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users?tab=users");
    }
}