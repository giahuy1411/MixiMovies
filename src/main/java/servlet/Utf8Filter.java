package servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import entity.Category;
import java.util.List;

/**
 * Filter dùng để thiết lập mã hóa UTF-8 cho toàn bộ request và response.
 * Giúp hiển thị tiếng Việt chính xác trên giao diện web.
 */
@WebFilter("/*")
public class Utf8Filter implements Filter {

    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // Provide global category list for navbar (only for page requests, not static assets)
        String uri = ((javax.servlet.http.HttpServletRequest) request).getRequestURI();
        if (!uri.contains(".") || uri.endsWith(".jsp")) {
            try {
                List<Category> categories = categoryDAO.findAllActive();
                request.setAttribute("globalCategoryList", categories);
            } catch (Exception e) {
                // Silently fail - navbar will show empty category list
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo nếu cần
    }

    @Override
    public void destroy() {
        // Hủy nếu cần
    }
}
