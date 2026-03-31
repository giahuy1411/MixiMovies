package dao;

import entity.Share;
import java.util.List;

public interface ShareDAO {

    void create(Share s);

    List<Share> findBySeries(Long seriesId);
}