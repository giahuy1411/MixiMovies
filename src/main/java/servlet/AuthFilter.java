package servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final String SECURITY_URI = "securityUri";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        HttpSession session = req.getSession(false);
        User user = (session != null)
                ? (User) session.getAttribute("user")
                : null;

        // ===== ADMIN =====
        if (uri.contains("/admin")) {
            if (user == null) {
                req.getSession(true).setAttribute(SECURITY_URI, uri);
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            if (!Boolean.TRUE.equals(user.getAdmin())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Ban khong co quyen truy cap trang nay");
                return;
            }
        }

        // ===== CẦN LOGIN =====
        boolean needLogin = uri.contains("/addComment"); // Các chức năng yêu cầu user phải đăng nhập

        if (needLogin && user == null) {
            req.getSession(true).setAttribute(SECURITY_URI, uri);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // ===== PUBLIC (Không yêu cầu login) =====
        // Mọi request còn lại bao gồm:
        // Trang chủ (/home), chi tiết phim (/watch), đăng nhập (/login), đăng ký (/register)
        // và tài nguyên tĩnh (css, js, images) đều được public.
        
        chain.doFilter(request, response);
    }
    
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}