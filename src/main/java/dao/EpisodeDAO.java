package dao;

import entity.Episode;
import java.util.List;

public interface EpisodeDAO {
    
    List<Episode> findBySeries(Long seriesId);

    List<Episode> findBySeriesAndSeason(Long seriesId, int season);

    Episode findById(Long id);

    void create(Episode episode);

    void update(Episode episode);

    void delete(Long id);
}