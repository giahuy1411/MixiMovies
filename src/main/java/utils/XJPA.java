package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class XJPA {
    private static EntityManagerFactory factory;

    static {
        try {
            // Đọc credentials từ env.properties (file bí mật, không push lên Git)
            Properties env = new Properties();
            InputStream is = XJPA.class.getClassLoader().getResourceAsStream("env.properties");

            if (is != null) {
                env.load(is);
                is.close();

                // Ghi đè các giá trị từ env.properties vào JPA config
                Map<String, String> overrides = new HashMap<>();
                overrides.put("javax.persistence.jdbc.url", env.getProperty("DB_URL"));
                overrides.put("javax.persistence.jdbc.user", env.getProperty("DB_USER"));
                overrides.put("javax.persistence.jdbc.password", env.getProperty("DB_PASSWORD"));

                factory = Persistence.createEntityManagerFactory("MovieDB", overrides);
                System.out.println("✅ Kết nối DB thành công (từ env.properties)");
            } else {
                // Fallback: dùng giá trị mặc định trong persistence.xml
                factory = Persistence.createEntityManagerFactory("MovieDB");
                System.out.println("⚠️ Không tìm thấy env.properties, dùng persistence.xml mặc định.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ LỖI KHỞI TẠO KẾT NỐI: " + e.getMessage());
        }
    }

    public static EntityManager getEntityManager() {
        if (factory == null) {
            throw new RuntimeException("EntityManagerFactory chưa được khởi tạo!");
        }
        return factory.createEntityManager();
    }
}