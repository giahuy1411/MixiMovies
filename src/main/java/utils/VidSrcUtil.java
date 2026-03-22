package utils;

public class VidSrcUtil {

    // Domain mặc định (có thể thay đổi nếu cần)
    private static String domain = "https://vsembed.ru";

    /**
     * Lấy URL embed cho phim từ IMDb ID
     * @param imdbId IMDb ID (vd: tt1375666)
     * @return URL embed hoặc null nếu imdbId không hợp lệ
     */
    public static String getEmbedUrl(String imdbId) {
        if (imdbId == null || imdbId.trim().isEmpty()) {
            return null;
        }
        return domain + "/embed/" + imdbId;
    }

    /**
     * Cho phép thay đổi domain khi cần (ví dụ khi domain cũ bị chặn)
     * @param newDomain domain mới (ví dụ: https://vidsrc.net)
     */
    public static void setDomain(String newDomain) {
        if (newDomain != null && !newDomain.trim().isEmpty()) {
            domain = newDomain;
        }
    }

    /**
     * Lấy domain hiện tại đang dùng
     */
    public static String getDomain() {
        return domain;
    }
}