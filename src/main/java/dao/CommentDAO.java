package dao;

import entity.Comment;
import java.util.List;

public interface CommentDAO {
    List<Comment> findByVideo(Long videoId);
    void create(Comment comment);
    void delete(Long id);
    // Có thể thêm các phương thức khác nếu cần
}