package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import entity.Video;
import utils.XJPA;

public class VideoDAOImpl implements VideoDAO {

	@Override
	public List<Video> findAll() {
		EntityManager em = XJPA.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v";
			return em.createQuery(jpql, Video.class).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findAllActive() {
		EntityManager em = XJPA.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v WHERE v.active = true";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Video findById(Long id) {
		EntityManager em = XJPA.getEntityManager();
		try {
			return em.find(Video.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public void create(Video video) {
		EntityManager em = XJPA.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(video);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Video video) {
		EntityManager em = XJPA.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(video);
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
			Video v = em.find(Video.class, id);
			if (v != null)
				em.remove(v);
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

			em.createQuery("UPDATE Video v SET v.views = COALESCE(v.views,0) + 1 WHERE v.id = :id")
					.setParameter("id", id).executeUpdate();

			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}
}