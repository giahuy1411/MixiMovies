package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import utils.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String redirect = req.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            req.getSession(true).setAttribute("redirectAfterLogin", redirect);
        }
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String id       = req.getParameter("id");
        String password = req.getParameter("password");

        if (id == null || id.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        User user = userDao.findById(id.trim());

        // Dùng PasswordUtil.verify() — hỗ trợ cả hash SHA-256 lẫn plain text cũ
        if (user == null || !PasswordUtil.verify(password, user.getPassword())) {
            req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        req.changeSessionId();
        session.setAttribute(utils.AuthUtil.SESSION_USER_KEY, user);
        
        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
        String securityUri = (String) session.getAttribute("securityUri");

        if (redirectUrl != null) {
            session.removeAttribute("redirectAfterLogin");
            resp.sendRedirect(req.getContextPath() + "/" + redirectUrl);
        } else if (securityUri != null) {
            session.removeAttribute("securityUri");
            // securityUri từ filter thường đã có sẵn prefix "/...",
            // tuy nhiên tuỳ cách redirect ở filter. AuthFilter dùng req.getServletPath(). 
            // Ta cần prepend context path.
            resp.sendRedirect(req.getContextPath() + securityUri);
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}