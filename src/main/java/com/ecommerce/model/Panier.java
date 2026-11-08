package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "panier")
public class Panier implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_panier")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation")
    private Date dateCreation = new Date();

    @OneToOne
    @JoinColumn(name = "id_internaute", nullable = false, unique = true)
    private Internaute internaute;

    /** Composition : les lignes n'existent que dans le panier. */
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LignePanier> lignes = new ArrayList<>();

    public Panier() { }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (LignePanier l : lignes) {
            total = total.add(l.getSousTotal());
        }
        return total;
    }

    public int getNombreArticles() {
        int n = 0;
        for (LignePanier l : lignes) {
            n += l.getQuantite();
        }
        return n;
    }

    public boolean isVide() { return lignes.isEmpty(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public Internaute getInternaute() { return internaute; }
    public void setInternaute(Internaute internaute) { this.internaute = internaute; }
    public List<LignePanier> getLignes() { return lignes; }
    public void setLignes(List<LignePanier> lignes) { this.lignes = lignes; }
}
