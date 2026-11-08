package com.ecommerce.dao;

import com.ecommerce.model.Panier;
import java.util.List;
import javax.persistence.EntityManager;

public class PanierDao extends AbstractJpaDao<Panier, Long> {

    public PanierDao(EntityManager em) {
        super(em, Panier.class);
    }

    public Panier findByInternaute(Long internauteId) {
        List<Panier> res = em.createQuery(
                "select p from Panier p where p.internaute.id = :id", Panier.class)
                .setParameter("id", internauteId)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
}
