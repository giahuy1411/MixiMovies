package dao;

import entity.Share;
import utils.XJPA;

import javax.persistence.EntityManager;
import java.util.List;

public class ShareDAOImpl implements ShareDAO {

    @Override
    public void create(Share s) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(s);
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
    public List<Share> findBySeries(Long seriesId) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Share s WHERE s.series.id = :sid", Share.class)
                    .setParameter("sid", seriesId).getResultList();
        } finally {
            em.close();
        }
    }
}