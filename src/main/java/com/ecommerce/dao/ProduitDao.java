package com.ecommerce.dao;

import com.ecommerce.model.Produit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProduitDao extends AbstractJpaDao<Produit, Long> {

    public ProduitDao(EntityManager em) {
        super(em, Produit.class);
    }

    /** Recherche multicriteres (tous les criteres sont optionnels). */
    public List<Produit> search(Long vitrineId, Long categorieId, String motCle) {
        StringBuilder jpql = new StringBuilder("select p from Produit p where p.id is not null");
        boolean hasKeyword = motCle != null && !motCle.trim().isEmpty();
        if (vitrineId != null) {
            jpql.append(" and p.vitrine.id = :vid");
        }
        if (categorieId != null) {
            jpql.append(" and p.categorie.id = :cid");
        }
        if (hasKeyword) {
            jpql.append(" and lower(p.nom) like :kw");
        }
        jpql.append(" order by p.nom");

        TypedQuery<Produit> q = em.createQuery(jpql.toString(), Produit.class);
        if (vitrineId != null) {
            q.setParameter("vid", vitrineId);
        }
        if (categorieId != null) {
            q.setParameter("cid", categorieId);
        }
        if (hasKeyword) {
            q.setParameter("kw", "%" + motCle.trim().toLowerCase() + "%");
        }
        return q.getResultList();
    }

    public long countCommandes(Long produitId) {
        return em.createQuery(
                "select count(l) from LigneCommande l where l.produit.id = :id", Long.class)
                .setParameter("id", produitId)
                .getSingleResult();
    }

    public void deleteLignesPanier(Long produitId) {
        em.createQuery("delete from LignePanier l where l.produit.id = :id")
          .setParameter("id", produitId)
          .executeUpdate();
    }
}
