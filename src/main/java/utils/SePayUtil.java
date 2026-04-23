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

    // API endpoint chính xác của SePay
    private static final String API_URL = "https://my.sepay.vn/userapi/transactions/list";

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
     * Kiểm tra giao dịch thanh toán qua API SePay
     * 
     * Endpoint: GET https://my.sepay.vn/userapi/transactions/list
     * Response format:
     * {
     *   "status": 200,
     *   "transactions": [
     *     {
     *       "id": "...",
     *       "transaction_content": "MMXP admin",
     *       "transferAmount": 10000,
     *       "amount_in": "10000.00",
     *       "amount_out": "0.00",
     *       ...
     *     }
     *   ]
     * }
     */
    public static boolean checkPaymentStatus(String orderCode, double amount) {
        try {
            // Gọi API lấy 20 giao dịch gần nhất, lọc theo số tiền vào
            URL url = new URL(API_URL + "?limit=20&amount_in=" + (long) amount);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            System.out.println("[SePayUtil] API Response Code: " + responseCode);

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("[SePayUtil] API Response: " + response.toString().substring(0, Math.min(500, response.length())));

                // Phân tích JSON theo cấu trúc SePay thực tế
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                
                if (jsonResponse.has("transactions") && jsonResponse.get("transactions").isJsonArray()) {
                    JsonArray transactions = jsonResponse.getAsJsonArray("transactions");
                    
                    for (JsonElement element : transactions) {
                        JsonObject tx = element.getAsJsonObject();
                        
                        // Lấy nội dung chuyển khoản
                        String content = "";
                        if (tx.has("transaction_content") && !tx.get("transaction_content").isJsonNull()) {
                            content = tx.get("transaction_content").getAsString();
                        }
                        
                        // Lấy số tiền vào (có 2 field: transferAmount hoặc amount_in)
                        double transferAmount = 0;
                        if (tx.has("transferAmount") && !tx.get("transferAmount").isJsonNull()) {
                            transferAmount = tx.get("transferAmount").getAsDouble();
                        } else if (tx.has("amount_in") && !tx.get("amount_in").isJsonNull()) {
                            try {
                                transferAmount = Double.parseDouble(tx.get("amount_in").getAsString());
                            } catch (NumberFormatException ignored) {}
                        }

                        System.out.println("[SePayUtil] Checking tx: content='" + content + "', amount=" + transferAmount);

                        // Kiểm tra nếu nội dung chứa mã đơn hàng và số tiền khớp
                        if (content.toUpperCase().contains(orderCode.toUpperCase()) && transferAmount >= amount) {
                            System.out.println("[SePayUtil] ✅ Payment MATCHED! orderCode=" + orderCode);
                            return true;
                        }
                    }
                }
            } else {
                // Đọc error response
                BufferedReader err = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder errResponse = new StringBuilder();
                String line;
                while ((line = err.readLine()) != null) {
                    errResponse.append(line);
                }
                err.close();
                System.out.println("[SePayUtil] API Error: " + errResponse.toString());
            }
        } catch (Exception e) {
            System.out.println("[SePayUtil] Payment check failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[SePayUtil] ❌ No matching payment found for orderCode=" + orderCode + ", amount=" + amount);
        return false;
    }
}

