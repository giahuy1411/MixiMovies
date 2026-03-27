package utils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tiện ích hỗ trợ lấy và parse các tham số từ HttpServletRequest.
 * Giúp mã nguồn Servlet gọn gàng hơn, tránh các lỗi NullPointerException 
 * hay NumberFormatException khi người dùng không truyền tham số hoặc truyền sai định dạng.
 */
public class ParamUtil {

    /**
     * Parse chuỗi (String) từ request.
     * @param request HttpServletRequest
     * @param name Tên tham số
     * @param defaultValue Giá trị mặc định nếu tham số bị null hoặc rỗng
     * @return Chuỗi đã lấy được hoặc giá trị mặc định
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
     * @param request HttpServletRequest
     * @param name Tên tham số
     * @param defaultValue Giá trị mặc định nếu tham số bị null hoặc không phải là số hợp lệ
     * @return Số nguyên hoặc giá trị mặc định
     */
    public static int getInt(HttpServletRequest request, String name, int defaultValue) {
        String value = getString(request, name, null);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Ignore and return default value
            }
        }
        return defaultValue;
    }

    /**
     * Parse số thực (Double) từ request.
     * @param request HttpServletRequest
     * @param name Tên tham số
     * @param defaultValue Giá trị mặc định
     * @return Số thực hoặc giá trị mặc định
     */
    public static double getDouble(HttpServletRequest request, String name, double defaultValue) {
        String value = getString(request, name, null);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                // Ignore and return default value
            }
        }
        return defaultValue;
    }

    /**
     * Parse giá trị logic (Boolean) từ request.
     * @param request HttpServletRequest
     * @param name Tên tham số
     * @param defaultValue Giá trị mặc định
     * @return Boolean hoặc giá trị mặc định
     */
    public static boolean getBoolean(HttpServletRequest request, String name, boolean defaultValue) {
        String value = getString(request, name, null);
        if (value != null) {
            // Sẽ trả về true nếu chuỗi là "true" (không phân biệt hoa/thường)
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    /**
     * Parse ngày tháng (Date) từ request theo định dạng chuỗi mẫu.
     * @param request HttpServletRequest
     * @param name Tên tham số
     * @param pattern Định dạng chuỗi (VD: "yyyy-MM-dd" hoặc "dd/MM/yyyy")
     * @param defaultValue Giá trị mặc định nếu tham số rỗng hoặc sai định dạng
     * @return Ngày hoặc giá trị mặc định
     */
    public static Date getDate(HttpServletRequest request, String name, String pattern, Date defaultValue) {
        String value = getString(request, name, null);
        if (value != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                // Đảm bảo không auto chuyển ngày không hợp lệ sang ngày hợp lệ (VD: 32/01 -> 01/02)
                sdf.setLenient(false);
                return sdf.parse(value);
            } catch (ParseException e) {
                // Ignore and return default value
            }
        }
        return defaultValue;
    }
}
