package dao;

import entity.Comment;
import utils.XJPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CommentDAOImpl implements CommentDAO {

    @Override
    public List<Comment> findBySeries(Long seriesId) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT c FROM Comment c WHERE c.series.id = :sid ORDER BY c.createdAt DESC";
            TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
            query.setParameter("sid", seriesId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Comment comment) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comment);
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
            Comment comment = em.find(Comment.class, id);
            if (comment != null)
                em.remove(comment);
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