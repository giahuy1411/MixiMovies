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

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // Bỏ qua các file tĩnh (css, js, images, ...) để tăng hiệu suất
        if (uri.contains(".css") || uri.contains(".js") || uri.contains(".png") || 
            uri.contains(".jpg") || uri.contains(".jpeg") || uri.contains(".woff") || uri.contains(".ttf")) {
            chain.doFilter(request, response);
            return;
        }

        User user = utils.AuthUtil.get(req);

        // Nếu user bị ban (Active = false) nhưng vẫn còn session, huỷ session.
        if (user != null && !Boolean.TRUE.equals(user.getActive())) {
            req.getSession().invalidate();
            user = null;
        }

        // ===== ADMIN =====
        if (uri.contains("/admin")) {
            if (!utils.AuthUtil.isLogin(req)) {
                req.getSession(true).setAttribute(SECURITY_URI, uri);
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            if (!utils.AuthUtil.isAdmin(req)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang quản trị này");
                return;
            }
        }

        // ===== CẦN LOGIN =====
        // Thêm các uri cần bắt buộc người dùng đăng nhập tại đây
        boolean needLogin = uri.contains("/addComment") 
                         || uri.contains("/favorite")
                         || uri.contains("/like")
                         || uri.contains("/share");

        if (needLogin && !utils.AuthUtil.isLogin(req)) {
            req.getSession(true).setAttribute(SECURITY_URI, uri);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // ===== PUBLIC (Không yêu cầu login) =====
        // Mọi request còn lại (home, watch, login, register, vv...)

        chain.doFilter(request, response);
    }
    
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}