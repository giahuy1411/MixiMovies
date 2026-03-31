package dao;

import entity.Series;
import utils.XJPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class SeriesDAOImpl implements SeriesDAO {

    @Override
    public List<Series> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.episodes";
            return em.createQuery(jpql, Series.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Series> findAllActive() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.episodes WHERE s.active = true";
            return em.createQuery(jpql, Series.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Series findById(Long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT s FROM Series s LEFT JOIN FETCH s.episodes WHERE s.id = :id";
            return em.createQuery(jpql, Series.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Series findBySlug(String slug) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT s FROM Series s LEFT JOIN FETCH s.episodes WHERE s.slug = :slug";
            TypedQuery<Series> query = em.createQuery(jpql, Series.class);
            query.setParameter("slug", slug);
            return query.getSingleResult();
        } catch (NoResultException e) {
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
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}