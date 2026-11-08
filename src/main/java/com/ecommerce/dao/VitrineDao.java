package com.ecommerce.dao;

import com.ecommerce.model.Vitrine;
import javax.persistence.EntityManager;

public class VitrineDao extends AbstractJpaDao<Vitrine, Long> {
    public VitrineDao(EntityManager em) {
        super(em, Vitrine.class);
    }
}
