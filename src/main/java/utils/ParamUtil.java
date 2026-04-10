package utils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Tiện ích hỗ trợ lấy và parse các tham số từ HttpServletRequest.
 * Giúp mã nguồn Servlet gọn gàng hơn, tránh các lỗi NullPointerException 
 * hay NumberFormatException khi người dùng không truyền tham số hoặc truyền sai định dạng.
 */
public class ParamUtil {

    /**
     * Tự động đổ dữ liệu từ request vào một Java Bean.
     * Sử dụng thư viện Apache Commons BeanUtils.
     */
    public static <T> T toBean(HttpServletRequest request, Class<T> clazz) {
        try {
            T bean = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.populate(bean, request.getParameterMap());
            return bean;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đổ dữ liệu vào Bean: " + clazz.getName(), e);
        }
    }

    /**
     * Parse chuỗi (String) từ request.
     */
    public static String getString(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    /**
     * Parse số nguyên (Integer) từ request.
     */
    public static int getInt(HttpServletRequest request, String name, int defaultValue) {
        String value = getString(request, name, null);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse số nguyên lớn (Long) từ request.
     */
    public static long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = getString(request, name, null);
        try {
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse số thực (Double) từ request.
     */
    public static double getDouble(HttpServletRequest request, String name, double defaultValue) {
        String value = getString(request, name, null);
        try {
            return value != null ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse giá trị logic (Boolean) từ request.
     */
    public static boolean getBoolean(HttpServletRequest request, String name, boolean defaultValue) {
        String value = getString(request, name, null);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    /**
     * Parse ngày tháng (Date) từ request theo định dạng chuỗi mẫu.
     */
    public static Date getDate(HttpServletRequest request, String name, String pattern, Date defaultValue) {
        String value = getString(request, name, null);
        if (value != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setLenient(false);
                return sdf.parse(value);
            } catch (ParseException e) {
                // Ignore and return default value
            }
        }
        return defaultValue;
    }

    /**
     * Escape HTML characters to prevent XSS/HTML Injection.
     */
    public static String htmlEscape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
