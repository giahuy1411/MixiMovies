package dao;

import java.util.List;

import entity.Favorite;
import entity.Video;

public interface FavoriteDAO {
	
	void create(Favorite f);

	List<Video> findVideoByUser(String userId);
	
	void deleteByUserAndVideo(String userId, Long videoId);
}