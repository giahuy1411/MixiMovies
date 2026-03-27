package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tiện ích hash mật khẩu bằng SHA-256.
 * Mật khẩu được lưu dạng hex 64 ký tự.
 */
public class PasswordUtil {

    /**
     * Hash mật khẩu bằng SHA-256, trả về chuỗi hex 64 ký tự.
     */
    public static String hash(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plainText.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Lỗi hash mật khẩu", e);
        }
    }

    /**
     * So sánh mật khẩu plain text với hash đã lưu.
     * Hỗ trợ cả mật khẩu plain text cũ (migration) — xóa sau khi đã migrate xong.
     */
    public static boolean verify(String plainText, String storedHash) {
        if (storedHash == null) return false;
        // Nếu stored là hash SHA-256 (64 ký tự hex) → so sánh hash
        if (storedHash.length() == 64 && storedHash.matches("[0-9a-f]+")) {
            return hash(plainText).equals(storedHash);
        }
        // Legacy: mật khẩu plain text cũ (tạm thời hỗ trợ)
        return plainText.equals(storedHash);
    }
}
