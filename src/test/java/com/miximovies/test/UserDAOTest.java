package com.miximovies.test;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * TÀI LIỆU KIỂM THỬ: USER DAO (XÁC THỰC NGƯỜI DÙNG)
 * 
 * - Vai trò SDLC: Phân tích & Thực thi bộ Test cho Module Authentication theo Test Plan.
 * - Mục tiêu: Kiểm tra dữ liệu đăng nhập, tạo Account, phân quyền Admin và Cấm Account.
 * - Đối chiếu: Bộ TestCases_Authentication.md
 */
public class UserDAOTest {

    private UserDAO userDAO;
    private User testUser;

    @Before
    public void setUp() {
        // Arrange - Cài đặt Account dùng tạm cho các Test Case 
        userDAO = new UserDAOImpl();
        String uniqueId = "test_user_" + System.currentTimeMillis();
        
        testUser = new User();
        testUser.setId(uniqueId);
        testUser.setEmail(uniqueId + "@miximovies.com");
        testUser.setPassword("TestPass@123");
        testUser.setFullname("Người Dùng QA Test");
        testUser.setAdmin(false);
        testUser.setActive(true);
    }

    @After
    public void tearDown() {
        // (Optional) Xóa Account sau khi Test nếu DAO có chức năng delete thực (UserDAO hiện tại không delete thực)
        // Hệ thống sẽ coi như rác test user.
    }

    /**
     * TC-AUTH-011: Tạo người dùng mới qua lệnh Register hợp lệ
     * Mức độ: Critical
     */
    @Test
    public void testCreateUser_ValidData() {
        // Act
        userDAO.create(testUser);

        // Assert
        User savedUser = userDAO.findById(testUser.getId());
        assertNotNull("Lỗi: User không được lưu chèn vào DataBase", savedUser);
        assertEquals("Lỗi: Email bị thay đổi trái phép", testUser.getEmail(), savedUser.getEmail());
        assertTrue("Lỗi: Khách tạo nick mặc định phải là Active = true", savedUser.getActive());
    }

    /**
     * TC-AUTH-001/002: Tìm kiếm dữ liệu người dùng khi Thực hiện Đăng nhập vào hệ thống
     * Theo logic Code DAO sử dụng findByIdOrEmail vì người dùng chọn Email.
     * Mức độ: Critical
     */
    @Test
    public void testFindByIdOrEmail_WithEmail() {
        // Arrange
        userDAO.create(testUser);

        // Act
        User foundUser = userDAO.findByIdOrEmail(testUser.getEmail());

        // Assert
        assertNotNull("Lỗi: User đăng nhập bằng Email nhưng trả về null!", foundUser);
        assertEquals("Lỗi: Rò rỉ nhầm ID tài khoản", testUser.getId(), foundUser.getId());
    }

    /**
     * TC-AUTH-004: Đăng nhập với tài khoản không tồn tại hoàn toàn
     * Mức độ: Major
     */
    @Test
    public void testFindByIdOrEmail_NonExisting() {
        // Act
        User foundUser = userDAO.findByIdOrEmail("scammer_nonexist@m.com");

        // Assert
        assertNull("Lỗi: Người dùng ảo nhưng lại trả về Dữ Liệu SQL?!", foundUser);
    }

    /**
     * ADMIN-ROLE: Cập nhật thông tin Profile người dùng
     * Mức độ: Critical
     */
    @Test
    public void testUpdateUser() {
        // Arrange
        userDAO.create(testUser);
        User updateUser = userDAO.findById(testUser.getId());
        updateUser.setFullname("Mixi QA Đã Sửa");

        // Act
        userDAO.update(updateUser);

        // Assert
        User updatedUser = userDAO.findById(testUser.getId());
        assertEquals("Lỗi: Tên FullName Cập nhật chưa thành công", "Mixi QA Đã Sửa", updatedUser.getFullname());
    }

    /**
     * ADMIN-ROLE / TC-AUTH-005: Khóa và cấm người dùng không tuân thủ (Ban System)
     * Thử thay đổi biến Active
     * Mức độ: Critical
     */
    @Test
    public void testSetActive_LockUser() {
        // Arrange
        userDAO.create(testUser);

        // Act 1: Admin gạt khoá thành False
        userDAO.setActive(testUser.getId(), false);

        // Assert 1
        User lockedUser = userDAO.findById(testUser.getId());
        assertFalse("Lỗi Nghiêm Trọng: Cấm User không thành công (Hệ thống Banned Bị Lủng)", lockedUser.getActive());

        // Act 2: Admin mở lại thành True
        userDAO.setActive(testUser.getId(), true);

        // Assert 2
        User unlockedUser = userDAO.findById(testUser.getId());
        assertTrue("Lỗi: Mở khóa User hoàn lương không thành công", unlockedUser.getActive());
    }

    /**
     * TC-AUTH-015: Khống chế SQL Injection ở khâu Check Account
     * Mức độ: Critical / Security
     */
    @Test
    public void testFindByIdOrEmail_SQLInjection() {
        // Act: Hack query 
        User injectedUser = userDAO.findByIdOrEmail("' OR '1'='1");

        // Assert: Nếu code thủng sẽ lấy đại Record đầu tiên. Yêu cầu là Null.
        assertNull("LỖI BẢO MẬT KHẨN CẤP: SQL Injection đã vượt qua Form Query!", injectedUser);
    }
}
