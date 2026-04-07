package com.miximovies.test;

import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Series;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * TÀI LIỆU KIỂM THỬ: SERIES DAO (QUẢN LÝ PHIM)
 * 
 * - Vai trò SDLC: Thực thi kiểm thử (Module cốt lõi nhất của MixiMovies)
 * - Mục tiêu: Đảm bảo luồng Dữ liệu phim hoạt động chính xác từ Admin đến UI Khách
 */
public class SeriesDAOTest {

    private SeriesDAO seriesDAO;
    private Series testSeries;

    @Before
    public void setUp() {
        // Arrange 
        seriesDAO = new SeriesDAOImpl();

        String uniqueSuffix = "_" + System.currentTimeMillis();
        testSeries = new Series();
        testSeries.setTitle("Phim Test Unit " + uniqueSuffix);
        testSeries.setSlug("phim-test-unit-" + uniqueSuffix);
        testSeries.setYear(2026);
        testSeries.setViews(0);
        testSeries.setActive(true);
    }

    @After
    public void tearDown() {
        // Cleanup tự động sau mỗi method test
        try {
            if (testSeries.getId() != null) {
                seriesDAO.delete(testSeries.getId());
            }
        } catch (Exception e) {}
    }

    /**
     * TC-SERIES-001: Admin tạo một phim mới với đầy đủ thông tin chuẩn
     * Mức độ: Critical
     */
    @Test
    public void testCreateSeries_Valid() {
        // Act
        seriesDAO.create(testSeries);

        // Assert
        Series saved = seriesDAO.findById(testSeries.getId());
        assertNotNull("Lỗi: Tính năng thêm phim thất bại ở Server", saved);
        assertEquals("Lỗi: Tên phim bị biến dạng sau khi lưu", testSeries.getTitle(), saved.getTitle());
        assertTrue("Lỗi: Phim tạo mới phải ở trạng thái Active = true", saved.getActive());
    }

    /**
     * TC-SERIES-002: Kiểm thử tính năng tìm phim qua ID hợp lệ (xem chi tiết phim)
     * Mức độ: Critical
     */
    @Test
    public void testFindById_Existing() {
        // Arrange
        seriesDAO.create(testSeries);
        
        // Act
        Series found = seriesDAO.findById(testSeries.getId());

        // Assert
        assertNotNull("Lỗi: Trả về Null dù ID có thật", found);
        assertEquals(testSeries.getTitle(), found.getTitle());
    }

    /**
     * TC-SERIES-003: Phân tích ngoại lệ khi tìm ID phim không tồn tại
     * Mức độ: Major
     */
    @Test
    public void testFindById_NonExisting() {
        // Act
        Series found = seriesDAO.findById(999999L);
        
        // Assert
        assertNull("Lỗi: Cần trả về Null để Servlet có thể Redirect Code 404", found);
    }

    /**
     * TC-SERIES-004: Lấy danh sách phim cho màn hình Home (Chỉ phim Active)
     * Mức độ: Critical
     */
    @Test
    public void testFindAllActive() {
        // Arrange
        seriesDAO.create(testSeries);
        
        // Act
        List<Series> list = seriesDAO.findAllActive();

        // Assert
        assertNotNull("Lỗi: Không trả được danh sách phim nào", list);
        assertTrue("Lỗi: Hàm findAllActive không hoạt động", list.size() > 0);
    }

    /**
     * TC-SERIES-005: Kiểm tra tính năng tự động cộng Lượt View
     * Mức độ: Major
     */
    @Test
    public void testIncreaseView() {
        // Arrange
        seriesDAO.create(testSeries);
        Long id = testSeries.getId();
        long oldViews = seriesDAO.findById(id).getViews();
        
        // Act
        seriesDAO.increaseView(id);
        
        // Assert
        long newViews = seriesDAO.findById(id).getViews();
        assertEquals("Lỗi: Không thể tăng Lượt View lên 1 đơn vị!", oldViews + 1, newViews);
    }

    /**
     * TC-SERIES-006: Sửa dữ liệu trạng thái phim / Edit Phim
     * Mức độ: Critical
     */
    @Test
    public void testUpdateSeries() {
        // Arrange
        seriesDAO.create(testSeries);
        Series target = seriesDAO.findById(testSeries.getId());
        
        // Act: Đổi tên phim
        target.setTitle("Phim Đã Sửa Admin");
        seriesDAO.update(target);

        // Assert
        Series updated = seriesDAO.findById(testSeries.getId());
        assertEquals("Lỗi: Hibernate Cập nhật thông tin thất bại", "Phim Đã Sửa Admin", updated.getTitle());
    }
}
