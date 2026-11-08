package com.ecommerce.bean;

import com.ecommerce.model.Commande;
import com.ecommerce.model.Panier;
import com.ecommerce.service.BusinessException;
import com.ecommerce.service.ShopService;
import com.ecommerce.util.FacesUtil;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/** Gestion du panier de l'internaute connecte. */
@ManagedBean(name = "panierBean")
@SessionScoped
public class PanierBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{shopService}")
    private ShopService shopService;

    @ManagedProperty("#{internauteBean}")
    private InternauteBean internauteBean;

    private Panier panier;

    public Panier getPanier() {
        if (panier == null && internauteBean.isAuthentifie()) {
            charger();
        }
        return panier;
    }

    public void charger() {
        panier = shopService.getPanier(internauteBean.getConnecte().getId());
    }

    public int getNbArticles() {
        Panier p = getPanier();
        return (p == null) ? 0 : p.getNombreArticles();
    }

    public String ajouter(Long produitId) {
        if (!internauteBean.isAuthentifie()) {
            FacesUtil.error("Veuillez vous connecter pour ajouter un produit au panier.");
            FacesUtil.keepMessages();
            return "login?faces-redirect=true";
        }
        try {
            shopService.ajouterAuPanier(internauteBean.getConnecte().getId(), produitId, 1);
            charger();
            FacesUtil.info("Produit ajoute au panier.");
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
        }
        return null;
    }

    public String mettreAJour(Long ligneId, int quantite) {
        try {
            shopService.modifierQuantite(internauteBean.getConnecte().getId(), ligneId, quantite);
            charger();
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
            charger();   // restaure les quantites affichees
        }
        return null;
    }

    public String retirer(Long ligneId) {
        try {
            shopService.retirerLigne(internauteBean.getConnecte().getId(), ligneId);
            charger();
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
        }
        return null;
    }

    public String vider() {
        shopService.viderPanier(internauteBean.getConnecte().getId());
        charger();
        FacesUtil.info("Panier vide.");
        return null;
    }

    public String valider() {
        try {
            Commande c = shopService.validerPanier(internauteBean.getConnecte().getId());
            charger();
            FacesUtil.info("Commande n\u00b0" + c.getId() + " validee. Merci pour votre achat !");
            FacesUtil.keepMessages();
            return "commandes?faces-redirect=true";
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
            charger();
            return null;
        }
    }

    public void setShopService(ShopService shopService) { this.shopService = shopService; }
    public void setInternauteBean(InternauteBean internauteBean) { this.internauteBean = internauteBean; }
}
