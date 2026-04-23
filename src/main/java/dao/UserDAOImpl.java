package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import entity.User;
import utils.XJPA;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT o FROM User o";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll(int page, int size) {
        EntityManager em = XJPA.getEntityManager();
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            TypedQuery<User> query = em.createQuery("SELECT o FROM User o", User.class);
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
            return em.createQuery("SELECT COUNT(o) FROM User o", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public User findById(String id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public User findByIdOrEmail(String idOrEmail) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.id = :value OR u.email = :value";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("value", idOrEmail);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void create(User entity) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
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
    public void update(User entity) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
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
    public void setActive(String id, boolean active) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                user.setActive(active);
                em.merge(user);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
            }
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
