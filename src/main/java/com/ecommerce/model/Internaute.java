package com.ecommerce.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/** L'internaute est le visiteur / client inscrit du site. */
@Entity
@Table(name = "internaute")
public class Internaute implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_internaute")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Column(length = 255)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "profil", nullable = false, length = 20)
    private Role profil = Role.CLIENT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_inscription")
    private Date dateInscription = new Date();

    /** Association 1-1 : un internaute possede un panier (composition). */
    @OneToOne(mappedBy = "internaute", cascade = CascadeType.ALL)
    private Panier panier;

    @OneToMany(mappedBy = "internaute")
    private List<Commande> commandes = new ArrayList<>();

    public Internaute() { }

    public Internaute(String nom, String prenom, String email, String motDePasse, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
    }

    public String getNomComplet() { return prenom + " " + nom; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Role getProfil() { return profil; }
    public void setProfil(Role profil) { this.profil = profil; }
    public Date getDateInscription() { return dateInscription; }
    public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }
    public Panier getPanier() { return panier; }
    public void setPanier(Panier panier) {
        this.panier = panier;
        if (panier != null) {
            panier.setInternaute(this);
        }
    }
    public List<Commande> getCommandes() { return commandes; }
    public void setCommandes(List<Commande> commandes) { this.commandes = commandes; }
}
