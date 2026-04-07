package com.miximovies.test;

import dao.CommentDAO;
import dao.CommentDAOImpl;
import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import entity.Comment;
import entity.Series;
import entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * TÀI LIỆU KIỂM THỬ: COMMENT DAO
 * 
 * - Vai trò SDLC: Ghi nhận kiểm thử Module Tương tác
 * - Mục tiêu: Đảm bảo ráng buộc khóa ngoại User & Series được xử lý chính xác
 * - Quy trình: Có 1 file Test Case riêng (TestCases_Comments.md) đối chiếu
 */
public class CommentDAOTest {

    private CommentDAO commentDAO;
    private Comment testComment;
    private User testUser;
    private Series testSeries;

    @Before
    public void setUp() {
        // Arrange: Cài đặt dữ liệu phụ thuộc
        commentDAO = new CommentDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        SeriesDAO seriesDAO = new SeriesDAOImpl();

        // 1. Tạo Test User
        testUser = new User();
        testUser.setId("usr_cmt_" + System.currentTimeMillis());
        testUser.setEmail("cmt" + System.currentTimeMillis() + "@test.com");
        testUser.setPassword("Test123@");
        testUser.setFullname("Test Comment User");
        testUser.setActive(true);
        userDAO.create(testUser);

        // 2. Tạo Test Series
        testSeries = new Series();
        testSeries.setTitle("Test Phim Comment");
        testSeries.setSlug("test-phim-cmt-" + System.currentTimeMillis());
        testSeries.setActive(true);
        seriesDAO.create(testSeries);

        // 3. Chuẩn bị Test Comment
        testComment = new Comment();
        testComment.setUser(testUser);
        testComment.setSeries(testSeries);
        testComment.setContent("Bình luận kiểm thử hệ thống");
    }

    @After
    public void tearDown() {
        // Dọn dẹp dữ liệu theo thứ tự ngược: Comment -> Series
        // (Bỏ qua User để mô phỏng thực tế nếu UserDAO không hỗ trợ Delete)
        try {
            if (testComment != null && testComment.getId() != null) {
                commentDAO.delete(testComment.getId());
            }
        } catch (Exception e) {}
        
        try {
            if (testSeries != null && testSeries.getId() != null) {
                new SeriesDAOImpl().delete(testSeries.getId());
            }
        } catch (Exception e) {}
    }

    /**
     * TC-CMT-001: Thêm bình luận liên kết hợp lệ với Phim và Người dùng hiện có
     * Mức độ: Critical
     */
    @Test
    public void testCreateComment_Valid() {
        // Act
        commentDAO.create(testComment);
        
        // Assert
        assertNotNull("Lỗi: Không có ID được sinh ra sau khi lưu Comment (Lỗi Hibernate)", testComment.getId());
    }

    /**
     * TC-CMT-002: Lấy danh sách bình luận của một bộ phim
     * Mức độ: Critical
     */
    @Test
    public void testFindBySeriesId() {
        // Arrange
        commentDAO.create(testComment);

        // Act
        List<Comment> list = commentDAO.findBySeries(testSeries.getId());

        // Assert
        assertNotNull("Lỗi: Hàm trả về danh sách null!", list);
        assertTrue("Lỗi: Không tìm thấy bình luận vừa tạo cho phim!", list.size() > 0);
    }

    /**
     * TC-CMT-003: Xoá bình luận (kiểm tra tính toàn vẹn)
     * Mức độ: Major
     */
    @Test
    public void testDeleteComment() {
        // Arrange
        commentDAO.create(testComment);
        Long id = testComment.getId();
        int initialCount = commentDAO.findBySeries(testSeries.getId()).size();
        
        // Act
        commentDAO.delete(id);
        
        // Assert
        List<Comment> left = commentDAO.findBySeries(testSeries.getId());
        assertTrue("Lỗi: Bình luận vẫn chưa bị xóa khỏi danh sách Phim!", left.size() < initialCount);
    }

    /**
     * TC-CMT-004: Bình luận truy vấn trên Phim không tồn tại
     * Mức độ: Minor
     */
    @Test
    public void testFindBySeries_NotExists() {
        // Act
        List<Comment> list = commentDAO.findBySeries(999999L);

        // Assert
        assertNotNull("Lỗi: Nên trả về danh sách rỗng thay vì Null để tránh NullPointerException!", list);
        assertEquals("Lỗi: Phim không tồn tại nhưng lại có bình luận!", 0, list.size());
    }
}