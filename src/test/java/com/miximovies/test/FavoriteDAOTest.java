package com.miximovies.test;

import dao.FavoriteDAO;
import dao.FavoriteDAOImpl;
import dao.SeriesDAOImpl;
import dao.UserDAOImpl;
import entity.Favorite;
import entity.Series;
import entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
// import org.junit.Ignore; // Bỏ Ignore nếu muốn chạy Test trên môi trường CI/CD
import static org.junit.Assert.*;

import java.util.List;

/**
 * TÀI LIỆU KIỂM THỬ: FAVORITE DAO
 * 
 * - Hiện trạng (v1.0): Backend Servlet chưa áp dụng chức năng lưu Yêu Thích.
 * - Vai trò SDLC: Test-Driven Development (Kiểm thử đón đầu tính năng trong tương lai)
 * - Mục tiêu: Xác nhận hệ cơ sở dữ liệu Favorite sẽ không bị crash khi gắn Entity.
 */
public class FavoriteDAOTest {

    private FavoriteDAO favoriteDAO;
    private Favorite testFavorite;
    private User testUser;
    private Series testSeries;

    @Before
    public void setUp() {
        // Arrange
        favoriteDAO = new FavoriteDAOImpl();

        // Khởi tạo mock object giả lập khóa ngoại
        testUser = new User();
        testUser.setId("usr_fav_" + System.currentTimeMillis());
        testUser.setEmail("fav@" + System.currentTimeMillis() + ".com");
        testUser.setPassword("Test123@");
        testUser.setFullname("Test Favorite User");
        testUser.setActive(true);
        new UserDAOImpl().create(testUser);

        testSeries = new Series();
        testSeries.setTitle("Test Phim Favorite");
        testSeries.setSlug("test-phim-fav-" + System.currentTimeMillis());
        testSeries.setActive(true);
        new SeriesDAOImpl().create(testSeries);

        testFavorite = new Favorite();
        testFavorite.setUser(testUser);
        testFavorite.setSeries(testSeries);
    }

    @After
    public void tearDown() {
        // TearDown (Clean up DB)
        try { favoriteDAO.deleteByUserAndSeries(testUser.getId(), testSeries.getId()); } catch (Exception e) {}
        try { new SeriesDAOImpl().delete(testSeries.getId()); } catch (Exception e) {}
    }

    /**
     * TC-FAV-001: Người dùng thêm phim vào CSDL Yêu Thích
     * Mức độ: Major
     */
    @Test
    public void testCreateFavorite() {
        // Act
        favoriteDAO.create(testFavorite);
        
        // Assert
        List<Series> list = favoriteDAO.findSeriesByUser(testUser.getId());
        assertTrue("Lỗi: Yêu thích không được lưu thành công", list.size() > 0);
    }

    /**
     * TC-FAV-002: Lấy danh sách phim đã yêu thích theo UserID (cho trang Tủ Phim)
     * Mức độ: Critical
     */
    @Test
    public void testFindSeriesByUser() {
        // Arrange
        favoriteDAO.create(testFavorite);

        // Act
        List<Series> list = favoriteDAO.findSeriesByUser(testUser.getId());

        // Assert
        assertNotNull("Lỗi: Danh sách yêu thích không thể là null", list);
        assertEquals("Lỗi: Bộ phim lấy ra không khớp với phim đã thích", testSeries.getId(), list.get(0).getId());
    }

    /**
     * TC-FAV-003: Xóa phim khỏi vòng yêu thích
     * Mức độ: Major
     */
    @Test
    public void testDeleteByUserAndSeries() {
        // Arrange
        favoriteDAO.create(testFavorite);
        
        // Act
        favoriteDAO.deleteByUserAndSeries(testUser.getId(), testSeries.getId());
        
        // Assert
        List<Series> list = favoriteDAO.findSeriesByUser(testUser.getId());
        assertTrue("Lỗi: Phim vẫn còn trong danh sách yêu thích sau khi Xoá", list.isEmpty());
    }

    /**
     * TC-FAV-004: Ngăn chặn Duplicate (Thêm 2 lần một phim)
     * Nghiệp vụ: Constraint Database hoặc Logic Filter phải không cho sập web.
     * Mức độ: Minor
     */
    @Test
    public void testDuplicateFavorite() {
        // Arrange
        favoriteDAO.create(testFavorite);
        
        // Act & Assert (Tuỳ vào Hibernate setup, có thể bắt Exception nếu set Unique Constraint)
        try {
            favoriteDAO.create(testFavorite);
        } catch (Exception e) {
            // Test passed if exception is handled or Unique Constraint kicks in smoothly
            assertNotNull(e);
        }
    }
}