package com.miximovies.test;

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import entity.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * TÀI LIỆU KIỂM THỬ: CATEGORY DAO
 * 
 * - Vai trò SDLC: Giai đoạn Kiểm thử (System Test / Integration Test)
 * - Mục tiêu: Đảm bảo module Thể loại hoạt động chính xác với Database
 * - Quy tắc rà soát lỗi: Các Test Case tự làm sạch dữ liệu sau khi chạy.
 */
public class CategoryDAOTest {

    private CategoryDAO categoryDAO;
    private Category testCategory;

    @Before
    public void setUp() {
        // Arrange (Chuẩn bị môi trường)
        categoryDAO = new CategoryDAOImpl();
        
        String uniqueSuffix = "_" + System.currentTimeMillis();
        testCategory = new Category();
        testCategory.setName("Thể loại Test" + uniqueSuffix);
        testCategory.setSlug("the-loai-test" + uniqueSuffix);
        testCategory.setActive(true);
    }

    @After
    public void tearDown() {
        // Dọn dẹp dữ liệu để không tạo rác trong Database (Tư duy chất lượng)
        try {
            if (testCategory != null && testCategory.getId() != null) {
                categoryDAO.delete(testCategory.getId());
            }
        } catch (Exception e) {
            // Log lỗi nếu cần thiết
        }
    }

    /**
     * TC-CAT-001: Tạo thể loại mới hợp lệ
     * Mức độ: Critical
     */
    @Test
    public void testCreateCategory() {
        // Act (Thực thi hành động)
        categoryDAO.create(testCategory);

        // Assert (Kiểm tra kết quả thực tế với mong đợi)
        Category saved = categoryDAO.findById(testCategory.getId());
        assertNotNull("Lỗi: Không tìm thấy Thể loại vừa thêm trong Database!", saved);
        assertEquals("Lỗi: Tên Thể loại không khớp!", testCategory.getName(), saved.getName());
    }

    /**
     * TC-CAT-002: Lấy danh sách toàn bộ Thể loại
     * Mức độ: Major
     */
    @Test
    public void testFindAll() {
        // Arrange
        categoryDAO.create(testCategory);

        // Act
        List<Category> list = categoryDAO.findAll();

        // Assert
        assertNotNull("Lỗi: Danh sách thể loại trả về là Null!", list);
        assertTrue("Lỗi: Danh sách thể loại đang trống!", list.size() > 0);
    }

    /**
     * TC-CAT-003: Tìm thể loại thông qua Slug (Bao phủ yêu cầu chuẩn SEO)
     * Mức độ: Critical
     */
    @Test
    public void testFindBySlug() {
        // Arrange
        categoryDAO.create(testCategory);

        // Act
        Category found = categoryDAO.findBySlug(testCategory.getSlug());

        // Assert
        assertNotNull("Lỗi: Không thể tìm Category dựa trên Slug!", found);
        assertEquals("Lỗi: Slug không khớp!", testCategory.getSlug(), found.getSlug());
    }

    /**
     * TC-CAT-004: Chỉnh sửa thông tin Thể loại hiện có
     * Mức độ: Critical
     */
    @Test
    public void testUpdateCategory() {
        // Arrange
        categoryDAO.create(testCategory);
        Category update = categoryDAO.findById(testCategory.getId());
        update.setName("Thể loại Đã Sửa");

        // Act
        categoryDAO.update(update);

        // Assert
        Category updated = categoryDAO.findById(testCategory.getId());
        assertEquals("Lỗi: Tên Thể loại không được cập nhật thành công!", "Thể loại Đã Sửa", updated.getName());
    }

    /**
     * TC-CAT-005: Xóa Thể loại ra khỏi hệ thống
     * Mức độ: Critical
     */
    @Test
    public void testDeleteCategory() {
        // Arrange
        categoryDAO.create(testCategory);
        Long id = testCategory.getId();

        // Act
        categoryDAO.delete(id);

        // Assert
        Category deleted = categoryDAO.findById(id);
        assertNull("Lỗi: Thể loại vẫn tồn tại trong Database sau khi xóa!", deleted);
    }
}
