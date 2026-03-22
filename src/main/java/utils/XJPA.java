package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class XJPA {
    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory("MovieDB");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LỖI KHỞI TẠO KẾT NỐI: " + e.getMessage());
        }
    }

    public static EntityManager getEntityManager() {
        if (factory == null) {
            throw new RuntimeException("EntityManagerFactory chưa được khởi tạo!");
        }
        return factory.createEntityManager();
    }
}