package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import utils.PasswordUtil;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        Boolean verified = (Boolean) session.getAttribute("otp_verified");

        if (verified == null || !verified) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        Boolean verified = (Boolean) session.getAttribute("otp_verified");
        String userId = (String) session.getAttribute("otp_userid");

        if (verified == null || !verified || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        User user = userDao.findById(userId);
        if (user != null) {
            // Kiểm tra xem có trùng mật khẩu cũ không
            String newHash = PasswordUtil.hash(newPassword);
            if (newHash.equals(user.getPassword())) {
                req.setAttribute("error", "Mật khẩu mới không được trùng với mật khẩu cũ!");
                req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
                return;
            }

            // Cập nhật mật khẩu mới
            user.setPassword(newHash);
            userDao.update(user);

            // Xóa thông tin bảo mật trong session
            session.removeAttribute("otp");
            session.removeAttribute("otp_userid");
            session.removeAttribute("otp_email");
            session.removeAttribute("otp_verified");
            session.removeAttribute("otp_time");

            req.setAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
        }
    }
}
