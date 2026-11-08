package com.ecommerce.dao;

import com.ecommerce.model.Categorie;
import javax.persistence.EntityManager;

public class CategorieDao extends AbstractJpaDao<Categorie, Long> {
    public CategorieDao(EntityManager em) {
        super(em, Categorie.class);
    }
}
