package com.ecommerce.dao;

import com.ecommerce.model.Internaute;
import java.util.List;
import javax.persistence.EntityManager;

public class InternauteDao extends AbstractJpaDao<Internaute, Long> {

    public InternauteDao(EntityManager em) {
        super(em, Internaute.class);
    }

    public Internaute findByEmail(String email) {
        List<Internaute> res = em.createQuery(
                "select i from Internaute i where i.email = :email", Internaute.class)
                .setParameter("email", email)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
}
