package com.ecommerce.bean;

import com.ecommerce.model.Internaute;
import com.ecommerce.model.Role;
import com.ecommerce.service.BusinessException;
import com.ecommerce.service.ShopService;
import com.ecommerce.util.FacesUtil;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/** Gestion des internautes : inscription, connexion, deconnexion. */
@ManagedBean(name = "internauteBean")
@SessionScoped
public class InternauteBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{shopService}")
    private ShopService shopService;

    // champs des formulaires
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String adresse;

    private Internaute connecte;

    public String inscrire() {
        try {
            connecte = shopService.inscrire(nom, prenom, email, motDePasse, adresse);
            FacesUtil.info("Bienvenue " + connecte.getNomComplet() + " ! Votre compte a ete cree.");
            FacesUtil.keepMessages();
            viderFormulaire();
            return "index?faces-redirect=true";
        } catch (BusinessException e) {
            FacesUtil.error(e.getMessage());
            return null;
        }
    }

    public String login() {
        Internaute i = shopService.authentifier(email, motDePasse);
        motDePasse = null;
        if (i == null) {
            FacesUtil.error("E-mail ou mot de passe incorrect.");
            return null;
        }
        connecte = i;
        viderFormulaire();
        FacesUtil.info("Connexion reussie.");
        FacesUtil.keepMessages();
        return "index?faces-redirect=true";
    }

    public String logout() {
        FacesUtil.invalidateSession();   // detruit aussi le panier en session
        return "index?faces-redirect=true";
    }

    private void viderFormulaire() {
        nom = null;
        prenom = null;
        email = null;
        motDePasse = null;
        adresse = null;
    }

    public boolean isAuthentifie() { return connecte != null; }
    public boolean isAdmin() { return connecte != null && connecte.getProfil() == Role.ADMIN; }
    public Internaute getConnecte() { return connecte; }

    public void setShopService(ShopService shopService) { this.shopService = shopService; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}
