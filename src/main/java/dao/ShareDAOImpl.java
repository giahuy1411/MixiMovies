package dao;

import java.util.List;

import javax.persistence.EntityManager;
import entity.Share;
import utils.XJPA;

public class ShareDAOImpl implements ShareDAO {

    @Override
    public void create(Share s) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Share> findByVideo(Long videoId) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery(
                "SELECT s FROM Share s WHERE s.video.id = :vid",
                Share.class
            ).setParameter("vid", videoId)
             .getResultList();
        } finally {
            em.close();
        }
    }
}