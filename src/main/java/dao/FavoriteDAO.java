package dao;

import entity.Favorite;
import entity.Series;
import java.util.List;

public interface FavoriteDAO {
	
	void create(Favorite f);

	List<Series> findSeriesByUser(String userId);

	void deleteByUserAndSeries(String userId, Long seriesId);

	boolean isFavorite(String userId, Long seriesId);
}