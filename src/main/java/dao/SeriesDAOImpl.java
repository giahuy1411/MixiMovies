package dao;

import entity.Series;
import utils.XJPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class SeriesDAOImpl implements SeriesDAO {

    @Override
    public List<Series> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category";
            return em.createQuery(jpql, Series.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Series> findAll(int page, int size, String sortBy, String sortDir) {
        EntityManager em = XJPA.getEntityManager();
        try {
            // Whitelist sort fields to prevent injection
            String validSort = "s.id";
            if ("title".equals(sortBy)) validSort = "s.title";
            else if ("year".equals(sortBy)) validSort = "s.year";
            else if ("views".equals(sortBy)) validSort = "s.views";
            else if ("createdAt".equals(sortBy)) validSort = "s.createdAt";

            String validDir = "ASC".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";

            String jpql = "SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category ORDER BY " + validSort + " " + validDir;
            TypedQuery<Series> query = em.createQuery(jpql, Series.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = XJPA.getEntityManager();
        try {
            return (Long) em.createQuery("SELECT COUNT(s) FROM Series s").getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Series> findByCategory(Long cid, String categoryName, int page, int size) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category WHERE (s.category.id = :cid OR s.genre LIKE :genrePattern) AND s.active = true ORDER BY s.createdAt DESC";
            TypedQuery<Series> query = em.createQuery(jpql, Series.class);
            query.setParameter("cid", cid);
            query.setParameter("genrePattern", "%" + categoryName + "%");
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByCategory(Long cid, String categoryName) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return (Long) em.createQuery("SELECT COUNT(s) FROM Series s WHERE (s.category.id = :cid OR s.genre LIKE :genrePattern) AND s.active = true")
                    .setParameter("cid", cid)
                    .setParameter("genrePattern", "%" + categoryName + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Series> findAllActive() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category WHERE s.active = true";
            return em.createQuery(jpql, Series.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Series> findAllActive(int page, int size) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category WHERE s.active = true ORDER BY s.createdAt DESC";
            TypedQuery<Series> query = em.createQuery(jpql, Series.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Series findById(Long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category WHERE s.id = :id";
            return em.createQuery(jpql, Series.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Series findBySlug(String slug) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT s FROM Series s LEFT JOIN FETCH s.episodes LEFT JOIN FETCH s.category WHERE s.slug = :slug";
            TypedQuery<Series> query = em.createQuery(jpql, Series.class);
            query.setParameter("slug", slug);
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Series series) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(series);
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
    public void update(Series series) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(series);
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
            Series series = em.find(Series.class, id);
            if (series != null) em.remove(series);
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
    public void increaseView(Long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Series s SET s.views = COALESCE(s.views,0) + 1 WHERE s.id = :id")
                    .setParameter("id", id).executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    @Override
    public List<Object[]> getViewsByGenre() {
        EntityManager em = XJPA.getEntityManager();
        try {
            // Thống kê tổng lượt xem theo thể loại
            String jpql = "SELECT s.genre, SUM(COALESCE(s.views, 0)) FROM Series s GROUP BY s.genre";
            return em.createQuery(jpql, Object[].class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countActive() {
        EntityManager em = XJPA.getEntityManager();
        try {
            return (Long) em.createQuery("SELECT COUNT(s) FROM Series s WHERE s.active = true").getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long getTotalViews() {
        EntityManager em = XJPA.getEntityManager();
        try {
            Object res = em.createQuery("SELECT SUM(s.views) FROM Series s").getSingleResult();
            return res != null ? ((Number) res).longValue() : 0L;
        } finally {
            em.close();
        }
    }
}