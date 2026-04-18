package utils;

import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CsrfUtil {

    private static final String CSRF_TOKEN_ATTR = "csrf_token";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static void setToken(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        String token = generateToken();
        session.setAttribute(CSRF_TOKEN_ATTR, token);
    }

    public static String getToken(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (String) session.getAttribute(CSRF_TOKEN_ATTR) : null;
    }

    public static boolean validateToken(HttpServletRequest req, String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        String sessionToken = getToken(req);
        return sessionToken != null && sessionToken.equals(token);
    }
}