package dao;

import entity.Category;
import utils.XJPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public List<Category> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.order ASC", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAllActive() {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c WHERE c.active = true ORDER BY c.order ASC", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Category findById(Long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Category findBySlug(String slug) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE c.slug = :slug";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("slug", slug);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Category category) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Category category) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
