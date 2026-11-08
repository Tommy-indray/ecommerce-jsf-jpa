package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "produit")
public class Produit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long id;

    /** Verrouillage optimiste (evite deux commandes concurrentes sur le meme stock). */
    @Version
    private int version;

    @Column(name = "ref_produit", nullable = false, unique = true, length = 50)
    private String reference;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "id_vitrine")
    private Vitrine vitrine;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    public Produit() { }

    public Produit(String reference, String nom, String description, BigDecimal prix, int stock) {
        this.reference = reference;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getVersion() { return version; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Vitrine getVitrine() { return vitrine; }
    public void setVitrine(Vitrine vitrine) { this.vitrine = vitrine; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
}
