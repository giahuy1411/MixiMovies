package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/verify-otp")
public class VerifyOTPServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        if (session.getAttribute("otp") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        
        req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String inputOtp = req.getParameter("otp");
        HttpSession session = req.getSession();
        String sessionOtp = (String) session.getAttribute("otp");
        Long otpTime = (Long) session.getAttribute("otp_time");

        if (sessionOtp == null || otpTime == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        // Kiểm tra timeout 5 phút (300,000 ms)
        if (System.currentTimeMillis() - otpTime > 300000) {
            session.removeAttribute("otp");
            req.setAttribute("error", "Mã OTP đã hết hiệu lực. Vui lòng yêu cầu mã mới!");
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
            return;
        }

        if (sessionOtp.equals(inputOtp)) {
            session.setAttribute("otp_verified", true);
            resp.sendRedirect(req.getContextPath() + "/reset-password");
        } else {
            req.setAttribute("error", "Mã xác thực không chính xác!");
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
        }
    }
}
