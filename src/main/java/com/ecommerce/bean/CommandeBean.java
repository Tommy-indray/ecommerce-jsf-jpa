package com.ecommerce.bean;

import com.ecommerce.model.Commande;
import com.ecommerce.service.ShopService;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/** Historique des commandes de l'internaute connecte. */
@ManagedBean(name = "commandeBean")
@RequestScoped
public class CommandeBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{shopService}")
    private ShopService shopService;

    @ManagedProperty("#{internauteBean}")
    private InternauteBean internauteBean;

    private List<Commande> commandes;

    public List<Commande> getCommandes() {
        if (commandes == null) {
            commandes = shopService.listerCommandes(internauteBean.getConnecte().getId());
        }
        return commandes;
    }

    public void setShopService(ShopService shopService) { this.shopService = shopService; }
    public void setInternauteBean(InternauteBean internauteBean) { this.internauteBean = internauteBean; }
}
