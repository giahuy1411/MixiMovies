package dao;

import entity.Comment;
import java.util.List;

public interface CommentDAO {

    List<Comment> findBySeries(Long seriesId);

    void create(Comment comment);

    void delete(Long id);
}