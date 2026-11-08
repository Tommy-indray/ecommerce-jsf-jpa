package com.ecommerce.dao;

import com.ecommerce.model.Commande;
import java.util.List;
import javax.persistence.EntityManager;

public class CommandeDao extends AbstractJpaDao<Commande, Long> {

    public CommandeDao(EntityManager em) {
        super(em, Commande.class);
    }

    public List<Commande> findByInternaute(Long internauteId) {
        return em.createQuery(
                "select c from Commande c where c.internaute.id = :id order by c.dateCommande desc",
                Commande.class)
                .setParameter("id", internauteId)
                .getResultList();
    }
}
