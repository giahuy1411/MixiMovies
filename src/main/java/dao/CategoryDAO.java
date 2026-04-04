package dao;

import entity.Category;
import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();
    List<Category> findAllActive();
    Category findById(Long id);
    Category findBySlug(String slug);
    void create(Category category);
    void update(Category category);
    void delete(Long id);
}
