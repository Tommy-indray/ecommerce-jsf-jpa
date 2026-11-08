package com.ecommerce.dao;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Implementation JPA generique. L'EntityManager est injecte par le constructeur
 * (Constructor Injection) : la gestion de la transaction reste dans la couche service.
 */
public abstract class AbstractJpaDao<T, ID> implements GenericDao<T, ID> {

    protected final EntityManager em;
    private final Class<T> entityClass;

    protected AbstractJpaDao(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    @Override
    public void create(T entity) {
        em.persist(entity);
    }

    @Override
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public T findById(ID id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass)
                 .getResultList();
    }
}
