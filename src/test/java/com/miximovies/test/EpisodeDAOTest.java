package com.miximovies.test;

import dao.EpisodeDAO;
import dao.EpisodeDAOImpl;
import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Episode;
import entity.Series;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * TÀI LIỆU KIỂM THỬ: EPISODE DAO
 * 
 * - Yêu cầu nghiệp vụ: Tập phim phải trực thuộc một Phim (Series).
 * - Tư duy chất lượng: Ràng buộc tính nhất quán dữ liệu giữa Series và Episode.
 */
public class EpisodeDAOTest {

    private EpisodeDAO episodeDAO;
    private Episode testEpisode;
    private Series testSeries;

    @Before
    public void setUp() {
        // Arrange
        episodeDAO = new EpisodeDAOImpl();
        SeriesDAO seriesDAO = new SeriesDAOImpl();

        String uniqueSuffix = "_" + System.currentTimeMillis();
        testSeries = new Series();
        testSeries.setTitle("Test Phim Episode" + uniqueSuffix);
        testSeries.setSlug("test-phim-episode" + uniqueSuffix);
        testSeries.setActive(true);
        seriesDAO.create(testSeries);

        testEpisode = new Episode();
        testEpisode.setSeries(testSeries);
        testEpisode.setEpisodeNumber(1);
        testEpisode.setTitle("Tập 1");
        testEpisode.setVideoUrl("https://kkphim.test/video.m3u8");
    }

    @After
    public void tearDown() {
        // Tắt liên kết và dọn dẹp Database
        try {
            if (testEpisode.getId() != null) {
                episodeDAO.delete(testEpisode.getId());
            }
        } catch (Exception e) {}
        
        try {
            if (testSeries != null && testSeries.getId() != null) {
                new SeriesDAOImpl().delete(testSeries.getId());
            }
        } catch (Exception e) {}
    }

    /**
     * TC-EP-001: Khởi tạo tập phim đầu tiên hợp lệ
     * Mức độ: Critical
     */
    @Test
    public void testCreateEpisode() {
        // Act
        episodeDAO.create(testEpisode);

        // Assert
        Episode saved = episodeDAO.findById(testEpisode.getId());
        assertNotNull("Lỗi: Tập phim không được tạo thành công", saved);
        assertEquals("Lỗi: Số tập không đúng", Integer.valueOf(1), saved.getEpisodeNumber());
    }

    /**
     * TC-EP-002: Lấy danh sách các tập phim thuộc về Series
     * Mức độ: Critical
     */
    @Test
    public void testFindBySeriesId() {
        // Arrange
        episodeDAO.create(testEpisode);
        
        // Act
        List<Episode> list = episodeDAO.findBySeries(testSeries.getId());
        
        // Assert
        assertNotNull("Lỗi: List trả về Null", list);
        assertTrue("Lỗi: Không tìm thấy tập phim thuộc Series này", list.size() > 0);
    }

    /**
     * TC-EP-003: Cập nhật thông tin tập phim (Đổi tên tập / Link Iframe)
     * Mức độ: Major
     */
    @Test
    public void testUpdateEpisode() {
        // Arrange
        episodeDAO.create(testEpisode);
        Episode episode = episodeDAO.findById(testEpisode.getId());
        
        // Act
        episode.setTitle("Tập 1 (Gốc) Đã Sửa");
        episodeDAO.update(episode);
        
        // Assert
        Episode updated = episodeDAO.findById(testEpisode.getId());
        assertEquals("Lỗi: Việc Edit Title của Episode không lưu vào Database", "Tập 1 (Gốc) Đã Sửa", updated.getTitle());
    }

    /**
     * TC-EP-004: Xoá tập phim khỏi DB
     * Mức độ: Critical
     */
    @Test
    public void testDeleteEpisode() {
        // Arrange
        episodeDAO.create(testEpisode);
        Long id = testEpisode.getId();
        
        // Act
        episodeDAO.delete(id);
        
        // Assert
        Episode deleted = episodeDAO.findById(id);
        assertNull("Lỗi: Giao thức Delete Hibernate không hoạt động trên Episode", deleted);
    }
}