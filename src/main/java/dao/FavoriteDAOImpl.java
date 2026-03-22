package dao;

import java.util.List;

import javax.persistence.EntityManager;
import entity.Favorite;
import entity.Video;
import utils.XJPA;

public class FavoriteDAOImpl implements FavoriteDAO {

	@Override
	public void create(Favorite f) {
		EntityManager em = XJPA.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(f);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findVideoByUser(String userId) {
		EntityManager em = XJPA.getEntityManager();
		try {
			return em.createQuery("SELECT f.video FROM Favorite f WHERE f.user.id = :uid", Video.class)
					.setParameter("uid", userId).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void deleteByUserAndVideo(String userId, Long videoId) {
		EntityManager em = XJPA.getEntityManager();
		try {
			em.getTransaction().begin();

			em.createQuery("DELETE FROM Favorite f WHERE f.user.id = :uid AND f.video.id = :vid")
					.setParameter("uid", userId).setParameter("vid", videoId).executeUpdate();

			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}
}