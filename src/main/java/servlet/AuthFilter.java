package servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;
import utils.AuthUtil;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final String SECURITY_URI = "securityUri";
    
    // Tập hợp các đuôi file tĩnh để lọc nhanh (O(1))
    private static final Set<String> STATIC_EXTENSIONS;

    static {
        Set<String> exts = new HashSet<>();
        exts.add("css"); exts.add("js"); exts.add("png"); exts.add("jpg");
        exts.add("jpeg"); exts.add("gif"); exts.add("svg"); exts.add("ico");
        exts.add("woff"); exts.add("ttf"); exts.add("map");
        STATIC_EXTENSIONS = Collections.unmodifiableSet(exts);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath().toLowerCase();
        
        // 1. Kiểm tra file tĩnh nhanh chóng
        int lastDot = path.lastIndexOf('.');
        if (lastDot != -1) {
            String extension = path.substring(lastDot + 1);
            if (STATIC_EXTENSIONS.contains(extension)) {
                chain.doFilter(request, response);
                return;
            }
        }

        User user = AuthUtil.get(req);

        // 2. Kiểm tra tính hợp lệ của Session (Trường hợp user bị khóa)
        if (user != null && !Boolean.TRUE.equals(user.getActive())) {
            req.getSession().invalidate();
            user = null;
        }

        // 3. Phân quyền Admin
        if (path.startsWith("/admin")) {
            if (!AuthUtil.isLogin(req)) {
                redirectToLogin(req, resp, path);
                return;
            }
            if (!AuthUtil.isAdmin(req)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập vùng quản trị.");
                return;
            }
        }

        // 4. Các đường dẫn yêu cầu đăng nhập (Whitelist)
        if (isProtectedPath(path)) {
            if (!AuthUtil.isLogin(req)) {
                redirectToLogin(req, resp, path);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isProtectedPath(String path) {
        return path.startsWith("/profile") || 
               path.startsWith("/favorite") || 
               path.startsWith("/addcomment") || 
               path.startsWith("/share") ||
               path.startsWith("/like") ||
               path.startsWith("/reset-password");
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp, String uri) throws IOException {
        req.getSession(true).setAttribute(SECURITY_URI, uri);
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}