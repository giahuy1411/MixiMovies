package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Filter để đảm bảo mọi Request và Response đều sử dụng bảng mã UTF-8.
 * Giúp hiển thị và nhập liệu tiếng Việt không bị lỗi font (mojibake).
 */
@WebFilter(filterName = "UTF8Filter", urlPatterns = "/*")
public class UTF8Filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo nếu cần
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Thiết lập encoding cho Request (dữ liệu từ Client gửi lên)
        request.setCharacterEncoding("UTF-8");
        
        // Thiết lập encoding cho Response (dữ liệu Server gửi về Client)
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Tiếp tục chuỗi Filter hoặc đến Servlet/JSP
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Hủy nếu cần
    }
}
