package servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import utils.Mailer;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String idOrEmail = req.getParameter("idOrEmail");
        if (idOrEmail == null || idOrEmail.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập Tên đăng nhập hoặc Email!");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        User user = userDao.findByIdOrEmail(idOrEmail);
        if (user == null) {
            req.setAttribute("error", "Không tìm thấy tài khoản với thông tin đã nhập!");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        // Tạo OTP 6 chữ số
        String otp = String.format("%06d", new Random().nextInt(1000000));
        
        // Lưu thông tin vào Session
        HttpSession session = req.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("otp_userid", user.getId());
        session.setAttribute("otp_email", user.getEmail());
        session.setAttribute("otp_time", System.currentTimeMillis()); // Cho bảo mật timeout (tùy chọn)

        // Gửi email
        String subject = "Mã xác thực khôi phục mật khẩu - MixiMovies";
        String body = "<h3>MixiMovies - Xác thực yêu cầu</h3>"
                    + "<p>Xin chào <b>" + user.getFullname() + "</b>,</p>"
                    + "<p>Mã OTP để khôi phục mật khẩu của bạn là: <b style='font-size: 1.5rem; color: #e50914;'>" + otp + "</b></p>"
                    + "<p>Vui lòng không cung cấp mã này cho bất kỳ ai. Mã có hiệu lực trong vòng 5 phút.</p>";

        try {
            Mailer.send("miximovies@noreply.com", user.getEmail(), subject, body);
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Gửi email thất bại. Vui lòng thử lại sau!");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
        }
    }
}
