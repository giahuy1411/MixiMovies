package dao;

import java.util.List;
import entity.Video;

public interface VideoDAO {

    List<Video> findAll();

    List<Video> findAllActive();

    Video findById(Long id);

    void create(Video video);

    void update(Video video);

    void delete(Long id);

    void increaseView(Long id);
}