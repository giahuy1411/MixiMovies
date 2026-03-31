package dao;

import entity.Series;
import java.util.List;

public interface SeriesDAO {
    
    List<Series> findAll();

    List<Series> findAllActive();

    Series findById(Long id);

    Series findBySlug(String slug);

    void create(Series series);

    void update(Series series);

    void delete(Long id);

    void increaseView(Long id);
}