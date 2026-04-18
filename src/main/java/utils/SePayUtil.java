package utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Tiện ích tích hợp API SePay (VietQR)
 * Hỗ trợ tạo mã QR và kiểm tra lịch sử giao dịch qua API
 */
public class SePayUtil {

    private static final String API_URL = "https://api.sepay.vn/v1/messages/list";

    private static final String API_TOKEN;
    private static final String BANK_NUMBER;
    private static final String BANK_CODE;
    private static final String ACCOUNT_NAME;

    static {
        Properties env = new Properties();
        try (InputStream is = SePayUtil.class.getClassLoader().getResourceAsStream("env.properties")) {
            if (is != null) {
                env.load(is);
            }
        } catch (Exception e) {
            // Ignore
        }
        API_TOKEN = env.getProperty("SEP_TOKEN", "");
        BANK_NUMBER = env.getProperty("SEP_BANK_NUMBER", "");
        BANK_CODE = env.getProperty("SEP_BANK_CODE", "mbbank");
        ACCOUNT_NAME = env.getProperty("SEP_ACCOUNT_NAME", "");
    }

    /**
     * Tạo URL mã QR thanh toán (VietQR)
     * 
     * @param orderCode Mã đơn hàng (để khách hàng ghi vào nội dung chuyển khoản)
     * @param amount    Số tiền thanh toán
     * @return URL ảnh mã QR
     */
    public static String generateQrUrl(String orderCode, double amount) {
        try {
            String description = URLEncoder.encode(orderCode, StandardCharsets.UTF_8.toString());
            return String.format("https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%d&des=%s",
                    BANK_NUMBER, BANK_CODE, (long) amount, description);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Kiểm tra giao dịch thanh toán qua API SePay (Polling)
     * 
     * @param orderCode Mã đơn hàng cần tìm
     * @param amount    Số tiền mong đợi
     * @return true nếu tìm thấy giao dịch thành công khớp với mã đơn hàng và số
     *         tiền
     */
    public static boolean checkPaymentStatus(String orderCode, double amount) {
        try {
            URL url = new URL(API_URL + "?limit=20"); // Lấy 20 giao dịch gần nhất
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Phân tích JSON
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                if (jsonResponse.has("messages")) {
                    JsonArray messages = jsonResponse.getAsJsonArray("messages");
                    for (JsonElement element : messages) {
                        JsonObject msg = element.getAsJsonObject();
                        String content = msg.get("content").getAsString();
                        double transferAmount = msg.get("amount_in").getAsDouble();

                        // Kiểm tra nếu nội dung chứa mã đơn hàng và số tiền khớp
                        if (content.toUpperCase().contains(orderCode.toUpperCase()) && transferAmount >= amount) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[SePayUtil] Payment check failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        return false;
    }
}
