package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import entity.User;

/**
 * Lớp tiện ích để kiểm tra trạng thái đăng nhập và quyền hạn của người dùng.
 * Giúp mã nguồn trong Servlet và JSP gọn gàng hơn.
 */
public class AuthUtil {

    /**
     * Lấy đối tượng người dùng hiện tại từ Session.
     */
    public static User get(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }

    /**
     * Kiểm tra xem người dùng đã đăng nhập hay chưa.
     */
    public static boolean isLogin(HttpServletRequest req) {
        return get(req) != null;
    }

    /**
     * Kiểm tra xem người dùng hiện tại có phải là Quản trị viên (Admin) hay không.
     */
    public static boolean isAdmin(HttpServletRequest req) {
        User user = get(req);
        return user != null && Boolean.TRUE.equals(user.getAdmin());
    }
}
