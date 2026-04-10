package dao;

import entity.Series;
import java.util.List;

public interface SeriesDAO {
    
    List<Series> findAll();
    
    /**
     * Phân trang và sắp xếp phim
     */
    List<Series> findAll(int page, int size, String sortBy, String sortDir);

    long count();

    List<Series> findAllActive();

    List<Series> findByCategory(Long cid, String categoryName, int page, int size);

    long countByCategory(Long cid, String categoryName);

    Series findById(Long id);

    Series findBySlug(String slug);

    void create(Series series);

    void update(Series series);

    void delete(Long id);

    void increaseView(Long id);

    /**
     * Thống kê lượt xem theo thể loại cho biểu đồ
     * Trả về List các Object[]: [Thể loại (String), Tổng lượt xem (Long)]
     */
    List<Object[]> getViewsByGenre();

    long countActive();

    long getTotalViews();
}