package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hash(String plainText) {
        return hash(plainText, generateSalt());
    }
    
    public static String hash(String plainText, String salt) {
        try {
            String salted = salt + plainText;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(salted.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return salt + "$" + sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Lỗi hash mật khẩu", e);
        }
    }

    public static boolean verify(String plainText, String storedHash) {
        if (plainText == null || storedHash == null) return false;
        if (!storedHash.contains("$")) {
            return plainText.equals(storedHash);
        }
        String[] parts = storedHash.split("\\$", 2);
        if (parts.length != 2) return false;
        String salt = parts[0];
        String hash = parts[1];
        return hash(plainText, salt).equals(storedHash);
    }

    private static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}