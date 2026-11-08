package com.ecommerce.bean;

import com.ecommerce.model.Categorie;
import com.ecommerce.model.Produit;
import com.ecommerce.model.Vitrine;
import com.ecommerce.service.BusinessException;
import com.ecommerce.service.ShopService;
import com.ecommerce.util.FacesUtil;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/** Gestion de la vitrine : consultation / filtrage du catalogue et administration des produits. */
@ManagedBean(name = "vitrineBean")
@ViewScoped
public class VitrineBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{shopService}")
    private ShopService shopService;

    // filtres
    private Long vitrineId;
    private Long categorieId;
    private String motCle;

    // formulaire d'ajout (admin)
    private Produit nouveau = new Produit();
    private Long formVitrineId;
    private Long formCategorieId;

    private List<Vitrine> vitrines;
    private List<Categorie> categories;
    private List<Produit> produits;

    public String filtrer() {
        produits = null;   // sera recharge au prochain affichage
        return null;
    }

    public String ajouterProduit() {
        try {
            shopService.ajouterProduit(nouveau, formVitrineId, formCategorieId);
            FacesUtil.info("Produit \"" + nouveau.getNom() + "\" ajoute.");
            nouveau = new Produit();
            produits = null;
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
        }
        return null;
    }

    public String supprimerProduit(Long produitId) {
        try {
            shopService.supprimerProduit(produitId);
            FacesUtil.info("Produit supprime.");
            produits = null;
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
        }
        return null;
    }

    public List<Vitrine> getVitrines() {
        if (vitrines == null) {
            vitrines = shopService.listerVitrines();
        }
        return vitrines;
    }

    public List<Categorie> getCategories() {
        if (categories == null) {
            categories = shopService.listerCategories();
        }
        return categories;
    }

    public List<Produit> getProduits() {
        if (produits == null) {
            produits = shopService.rechercherProduits(vitrineId, categorieId, motCle);
        }
        return produits;
    }

    public void setShopService(ShopService shopService) { this.shopService = shopService; }
    public Long getVitrineId() { return vitrineId; }
    public void setVitrineId(Long vitrineId) { this.vitrineId = vitrineId; }
    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }
    public String getMotCle() { return motCle; }
    public void setMotCle(String motCle) { this.motCle = motCle; }
    public Produit getNouveau() { return nouveau; }
    public void setNouveau(Produit nouveau) { this.nouveau = nouveau; }
    public Long getFormVitrineId() { return formVitrineId; }
    public void setFormVitrineId(Long formVitrineId) { this.formVitrineId = formVitrineId; }
    public Long getFormCategorieId() { return formCategorieId; }
    public void setFormCategorieId(Long formCategorieId) { this.formCategorieId = formCategorieId; }
}
