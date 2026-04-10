package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import entity.User;

/**
 * Lớp tiện ích để kiểm tra trạng thái đăng nhập và quyền hạn của người dùng.
 */
public class AuthUtil {

    // Hằng số cho khóa lưu trữ người dùng trong Session
    public static final String SESSION_USER_KEY = "user";

    /**
     * Lấy đối tượng người dùng hiện tại từ Session.
     */
    public static User get(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(SESSION_USER_KEY);
        }
        return null;
    }

    /**
     * Kiểm tra xem người dùng đã đăng nhập và tài khoản CÒN HOẠT ĐỘNG.
     */
    public static boolean isLogin(HttpServletRequest req) {
        User user = get(req);
        return user != null && Boolean.TRUE.equals(user.getActive());
    }

    /**
     * Kiểm tra quyền Quản trị (Admin) và trạng thái hoạt động.
     */
    public static boolean isAdmin(HttpServletRequest req) {
        User user = get(req);
        return isLogin(req) && Boolean.TRUE.equals(user.getAdmin());
    }

    /**
     * Kiểm tra xem người dùng hiện tại có phải là chủ sở hữu của một ID (ví dụ: userId của Comment) hay không.
     * Hữu ích cho việc cho phép người dùng xóa/sửa nội dung của chính họ.
     */
    public static boolean isOwner(String ownerId, HttpServletRequest req) {
        User user = get(req);
        if (user == null || ownerId == null) return false;
        return user.getId().equals(ownerId);
    }
}
