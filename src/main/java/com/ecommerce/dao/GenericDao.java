package com.ecommerce.dao;

import java.util.List;

/** Contrat CRUD generique (principes SOLID : ISP / DIP - on depend de l'interface). */
public interface GenericDao<T, ID> {
    void create(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(ID id);
    List<T> findAll();
}
