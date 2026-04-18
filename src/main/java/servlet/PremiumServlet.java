package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import utils.AuthUtil;
import utils.SePayUtil;

@WebServlet({"/premium", "/premium/checkout", "/premium/confirm"})
public class PremiumServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        User user = AuthUtil.get(req);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=premium");
            return;
        }

        if (uri.contains("/checkout")) {
            String plan = req.getParameter("plan");
            double amount = 10000;
            String planName = "Gói tháng (Premium)";
            
            if ("year".equals(plan)) {
                amount = 20000;
                planName = "Gói năm (Premium)";
            }
            
            // Mã đơn hàng định dạng: MMXP[UserId]
            String orderCode = "MMXP" + user.getId();
            String qrUrl = SePayUtil.generateQrUrl(orderCode, amount);
            
            req.setAttribute("amount", (long)amount);
            req.setAttribute("planName", planName);
            req.setAttribute("plan", plan);
            req.setAttribute("orderCode", orderCode);
            req.setAttribute("qrUrl", qrUrl);
            
            req.getRequestDispatcher("/views/checkout.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/views/premium.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.contains("/confirm")) {
            User user = AuthUtil.get(req);
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            String plan = req.getParameter("plan");
            double amount = "year".equals(plan) ? 500000 : 50000;
            String orderCode = "MMXP" + user.getId();

            // Kiểm tra trạng thái thanh toán thực tế qua SePay API
            boolean isPaid = SePayUtil.checkPaymentStatus(orderCode, amount);

            if (isPaid) {
                user.setPremium(true);
                userDAO.update(user);
                
                // Cập nhật session
                req.getSession().setAttribute("user", user);
                
                req.setAttribute("message", "Chúc mừng! Bạn đã nâng cấp Premium thành công. Hãy tận hưởng mọi bộ phim mới nhất.");
                req.getRequestDispatcher("/views/payment-success.jsp").forward(req, resp);
            } else {
                // Nếu chưa thanh toán, quay lại trang checkout và báo lỗi
                String planName = "year".equals(plan) ? "Gói năm (Premium)" : "Gói tháng (Premium)";
                req.setAttribute("error", "Hệ thống chưa nhận được thanh toán của bạn cho nội dung " + orderCode + ". Vui lòng đợi 1-2 phút và thử lại, hoặc liên hệ hỗ trợ.");
                req.setAttribute("amount", (long)amount);
                req.setAttribute("planName", planName);
                req.setAttribute("plan", plan);
                req.setAttribute("orderCode", orderCode);
                req.setAttribute("qrUrl", SePayUtil.generateQrUrl(orderCode, amount));
                req.getRequestDispatcher("/views/checkout.jsp").forward(req, resp);
            }
        }
    }
}
