package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

/** Classe d'association entre Panier et Produit (attribut : quantite). */
@Entity
@Table(name = "ligne_panier",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_panier", "id_produit"}))
public class LignePanier implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ligne")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_panier")
    private Panier panier;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_produit")
    private Produit produit;

    @Column(nullable = false)
    private int quantite;

    public LignePanier() { }

    public LignePanier(Panier panier, Produit produit, int quantite) {
        this.panier = panier;
        this.produit = produit;
        this.quantite = quantite;
    }

    public BigDecimal getSousTotal() {
        return produit.getPrix().multiply(BigDecimal.valueOf(quantite));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Panier getPanier() { return panier; }
    public void setPanier(Panier panier) { this.panier = panier; }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}
