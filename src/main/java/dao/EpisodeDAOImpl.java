package dao;

import entity.Episode;
import utils.XJPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EpisodeDAOImpl implements EpisodeDAO {

    @Override
    public List<Episode> findBySeries(Long seriesId) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT e FROM Episode e WHERE e.series.id = :sid ORDER BY e.seasonNumber, e.episodeNumber";
            TypedQuery<Episode> query = em.createQuery(jpql, Episode.class);
            query.setParameter("sid", seriesId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Episode> findBySeriesAndSeason(Long seriesId, int season) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT e FROM Episode e WHERE e.series.id = :sid AND e.seasonNumber = :season ORDER BY e.episodeNumber";
            TypedQuery<Episode> query = em.createQuery(jpql, Episode.class);
            query.setParameter("sid", seriesId);
            query.setParameter("season", season);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Episode findById(Long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.find(Episode.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Episode episode) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(episode);
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
    public void update(Episode episode) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(episode);
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
            Episode episode = em.find(Episode.class, id);
            if (episode != null) em.remove(episode);
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