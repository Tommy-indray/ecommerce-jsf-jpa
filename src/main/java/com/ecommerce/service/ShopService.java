package com.ecommerce.service;

import com.ecommerce.dao.CategorieDao;
import com.ecommerce.dao.CommandeDao;
import com.ecommerce.dao.InternauteDao;
import com.ecommerce.dao.PanierDao;
import com.ecommerce.dao.ProduitDao;
import com.ecommerce.dao.VitrineDao;
import com.ecommerce.model.Categorie;
import com.ecommerce.model.Commande;
import com.ecommerce.model.Internaute;
import com.ecommerce.model.LigneCommande;
import com.ecommerce.model.LignePanier;
import com.ecommerce.model.Panier;
import com.ecommerce.model.Produit;
import com.ecommerce.model.Vitrine;
import com.ecommerce.util.JPAUtil;
import com.ecommerce.util.PasswordUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 * Pattern Facade : point d'entree unique de la logique metier pour les managed beans JSF.
 * Chaque methode = une unite de travail (un EntityManager + une transaction).
 * Declaree comme managed bean (portee application) dans faces-config.xml puis injectee
 * dans les beans par @ManagedProperty (Dependency Injection).
 */
public class ShopService implements Serializable {
    private static final long serialVersionUID = 1L;

    // ======================= INTERNAUTE =======================

    public Internaute inscrire(String nom, String prenom, String email, String motDePasse, String adresse) {
        final String mail = email.trim().toLowerCase();
        try {
            return JPAUtil.callInTransaction(em -> {
                InternauteDao dao = new InternauteDao(em);
                if (dao.findByEmail(mail) != null) {
                    throw new BusinessException("Cette adresse e-mail est deja utilisee.");
                }
                Internaute i = new Internaute(nom.trim(), prenom.trim(), mail, PasswordUtil.hash(motDePasse), adresse);
                i.setPanier(new Panier());   // un panier vide est cree avec l'internaute
                dao.create(i);               // cascade PERSIST sur le panier
                return i;
            });
        } catch (PersistenceException e) {
            throw new BusinessException("Inscription impossible : " + e.getMessage());
        }
    }

    /** Retourne l'internaute authentifie, ou null si identifiants incorrects. */
    public Internaute authentifier(String email, String motDePasse) {
        return JPAUtil.query(em -> {
            Internaute i = new InternauteDao(em).findByEmail(email.trim().toLowerCase());
            return (i != null && PasswordUtil.verify(motDePasse, i.getMotDePasse())) ? i : null;
        });
    }

    // ======================= VITRINE =======================

    public List<Vitrine> listerVitrines() {
        return JPAUtil.query(em -> new VitrineDao(em).findAll());
    }

    public List<Categorie> listerCategories() {
        return JPAUtil.query(em -> new CategorieDao(em).findAll());
    }

    public List<Produit> rechercherProduits(Long vitrineId, Long categorieId, String motCle) {
        return JPAUtil.query(em -> new ProduitDao(em).search(vitrineId, categorieId, motCle));
    }

    public void ajouterProduit(Produit p, Long vitrineId, Long categorieId) {
        if (p.getPrix() == null || p.getPrix().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Le prix doit etre superieur a 0.");
        }
        if (p.getStock() < 0) {
            throw new BusinessException("Le stock ne peut pas etre negatif.");
        }
        try {
            JPAUtil.runInTransaction(em -> {
                Vitrine v = new VitrineDao(em).findById(vitrineId);
                Categorie c = new CategorieDao(em).findById(categorieId);
                if (v == null || c == null) {
                    throw new BusinessException("Vitrine ou categorie invalide.");
                }
                Produit nouveau = new Produit(p.getReference().trim(), p.getNom().trim(),
                        p.getDescription(), p.getPrix(), p.getStock());
                nouveau.setVitrine(v);
                nouveau.setCategorie(c);
                new ProduitDao(em).create(nouveau);
            });
        } catch (PersistenceException e) {
            throw new BusinessException("Enregistrement impossible (reference deja utilisee ?).");
        }
    }

    public void supprimerProduit(Long produitId) {
        try {
            JPAUtil.runInTransaction(em -> {
                ProduitDao dao = new ProduitDao(em);
                if (dao.countCommandes(produitId) > 0) {
                    throw new BusinessException("Ce produit figure dans des commandes : suppression impossible.");
                }
                dao.deleteLignesPanier(produitId);
                Produit p = dao.findById(produitId);
                if (p != null) {
                    dao.delete(p);
                }
            });
        } catch (PersistenceException e) {
            throw new BusinessException("Suppression impossible : " + e.getMessage());
        }
    }

    // ======================= PANIER =======================

    public Panier getPanier(Long internauteId) {
        return JPAUtil.query(em -> {
            Panier p = new PanierDao(em).findByInternaute(internauteId);
            if (p == null) {
                throw new BusinessException("Panier introuvable.");
            }
            return p;
        });
    }

    public void ajouterAuPanier(Long internauteId, Long produitId, int quantite) {
        if (quantite <= 0) {
            throw new BusinessException("La quantite doit etre positive.");
        }
        JPAUtil.runInTransaction(em -> {
            Panier panier = new PanierDao(em).findByInternaute(internauteId);
            Produit produit = new ProduitDao(em).findById(produitId);
            if (panier == null || produit == null) {
                throw new BusinessException("Produit ou panier introuvable.");
            }
            LignePanier existante = null;
            for (LignePanier l : panier.getLignes()) {
                if (l.getProduit().getId().equals(produitId)) {
                    existante = l;
                    break;
                }
            }
            int nouvelleQte = quantite + (existante == null ? 0 : existante.getQuantite());
            verifierStock(produit, nouvelleQte);
            if (existante != null) {
                existante.setQuantite(nouvelleQte);
            } else {
                LignePanier ligne = new LignePanier(panier, produit, quantite);
                panier.getLignes().add(ligne);
                em.persist(ligne);
            }
        });
    }

    public void modifierQuantite(Long internauteId, Long ligneId, int quantite) {
        JPAUtil.runInTransaction(em -> {
            LignePanier ligne = ligneDe(em.find(LignePanier.class, ligneId), internauteId);
            if (quantite <= 0) {
                ligne.getPanier().getLignes().remove(ligne);   // orphanRemoval => DELETE
            } else {
                verifierStock(ligne.getProduit(), quantite);
                ligne.setQuantite(quantite);
            }
        });
    }

    public void retirerLigne(Long internauteId, Long ligneId) {
        JPAUtil.runInTransaction(em -> {
            LignePanier ligne = ligneDe(em.find(LignePanier.class, ligneId), internauteId);
            ligne.getPanier().getLignes().remove(ligne);
        });
    }

    public void viderPanier(Long internauteId) {
        JPAUtil.runInTransaction(em -> {
            Panier panier = new PanierDao(em).findByInternaute(internauteId);
            if (panier != null) {
                panier.getLignes().clear();
            }
        });
    }

    // ======================= COMMANDES =======================

    /** Transforme le panier en commande, decremente les stocks, vide le panier (1 seule transaction). */
    public Commande validerPanier(Long internauteId) {
        try {
            return JPAUtil.callInTransaction(em -> {
                Panier panier = new PanierDao(em).findByInternaute(internauteId);
                if (panier == null || panier.getLignes().isEmpty()) {
                    throw new BusinessException("Votre panier est vide.");
                }
                Commande commande = new Commande(panier.getInternaute());
                for (LignePanier l : new ArrayList<>(panier.getLignes())) {
                    Produit p = l.getProduit();
                    verifierStock(p, l.getQuantite());
                    p.setStock(p.getStock() - l.getQuantite());
                    commande.addLigne(new LigneCommande(p, l.getQuantite(), p.getPrix()));
                }
                new CommandeDao(em).create(commande);
                panier.getLignes().clear();
                return commande;
            });
        } catch (PersistenceException e) {
            // ex. OptimisticLockException : le stock a change entre-temps
            throw new BusinessException("La commande n'a pas pu etre validee (stock modifie). Reessayez.");
        }
    }

    public List<Commande> listerCommandes(Long internauteId) {
        return JPAUtil.query(em -> new CommandeDao(em).findByInternaute(internauteId));
    }

    // ======================= outils prives =======================

    private void verifierStock(Produit p, int quantiteDemandee) {
        if (quantiteDemandee > p.getStock()) {
            throw new BusinessException("Stock insuffisant pour \"" + p.getNom()
                    + "\" (disponible : " + p.getStock() + ").");
        }
    }

    private LignePanier ligneDe(LignePanier ligne, Long internauteId) {
        if (ligne == null || !ligne.getPanier().getInternaute().getId().equals(internauteId)) {
            throw new BusinessException("Ligne de panier introuvable.");
        }
        return ligne;
    }
}
