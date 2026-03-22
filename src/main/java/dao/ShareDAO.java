package dao;

import java.util.List;

import entity.Share;

public interface ShareDAO {
	
    void create(Share s);
    
    List<Share> findByVideo(Long videoId);
}
