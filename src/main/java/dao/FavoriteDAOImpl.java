package dao;

import entity.Favorite;
import entity.Series;
import utils.XJPA;

import javax.persistence.EntityManager;
import java.util.List;

public class FavoriteDAOImpl implements FavoriteDAO {

	@Override
	public void create(Favorite f) {
		EntityManager em = XJPA.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(f);
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
	public List<Series> findSeriesByUser(String userId) {
		EntityManager em = XJPA.getEntityManager();
		try {
			return em.createQuery("SELECT f.series FROM Favorite f WHERE f.user.id = :uid", Series.class)
					.setParameter("uid", userId).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void deleteByUserAndSeries(String userId, Long seriesId) {
		EntityManager em = XJPA.getEntityManager();
		try {
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Favorite f WHERE f.user.id = :uid AND f.series.id = :sid")
					.setParameter("uid", userId).setParameter("sid", seriesId).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean isFavorite(String userId, Long seriesId) {
		EntityManager em = XJPA.getEntityManager();
		try {
			Long count = em.createQuery("SELECT COUNT(f) FROM Favorite f WHERE f.user.id = :uid AND f.series.id = :sid", Long.class)
					.setParameter("uid", userId).setParameter("sid", seriesId).getSingleResult();
			return count != null && count > 0;
		} finally {
			em.close();
		}
	}
}