package com.ecommerce.service;

import com.ecommerce.dao.InternauteDao;
import com.ecommerce.dao.VitrineDao;
import com.ecommerce.model.Categorie;
import com.ecommerce.model.Internaute;
import com.ecommerce.model.Panier;
import com.ecommerce.model.Produit;
import com.ecommerce.model.Role;
import com.ecommerce.model.Vitrine;
import com.ecommerce.util.JPAUtil;
import com.ecommerce.util.PasswordUtil;
import java.math.BigDecimal;

/** Insere des donnees de demonstration si la base est vide. */
public final class DataInitializer {

    private DataInitializer() { }

    public static void init() {
        JPAUtil.runInTransaction(em -> {
            if (!new VitrineDao(em).findAll().isEmpty()) {
                return;   // deja initialisee
            }
            Vitrine vitrine = new Vitrine("Boutique principale", "Les meilleures offres high-tech");

            Categorie info = new Categorie("Informatique", "Ordinateurs et peripheriques");
            Categorie tel = new Categorie("Telephonie", "Smartphones et tablettes");
            Categorie acc = new Categorie("Accessoires", "Casques, souris, claviers...");
            em.persist(info);
            em.persist(tel);
            em.persist(acc);

            ajouter(vitrine, info, "PC-001", "Laptop Pro 15\"", "Processeur 8 coeurs, 16 Go RAM, SSD 512 Go", "8990.00", 10);
            ajouter(vitrine, info, "PC-002", "Laptop Ecole 14\"", "Ideal pour les etudiants, 8 Go RAM, SSD 256 Go", "4590.00", 15);
            ajouter(vitrine, info, "PC-003", "Ecran 27 pouces", "Dalle IPS QHD 75 Hz", "2290.00", 8);
            ajouter(vitrine, tel, "TL-001", "Smartphone X10", "Ecran AMOLED 6.5\", 128 Go", "3990.00", 20);
            ajouter(vitrine, tel, "TL-002", "Tablette T8", "Tablette 10 pouces, 64 Go", "1990.00", 12);
            ajouter(vitrine, acc, "AC-001", "Casque Bluetooth", "Reduction de bruit active, 30 h d'autonomie", "790.00", 30);
            ajouter(vitrine, acc, "AC-002", "Souris sans fil", "Ergonomique, 2400 DPI", "149.00", 50);
            ajouter(vitrine, acc, "AC-003", "Clavier mecanique", "Switchs rouges, retroeclaire", "590.00", 25);
            em.persist(vitrine);   // cascade PERSIST sur les produits

            InternauteDao dao = new InternauteDao(em);
            dao.create(compte("Alaoui", "Sara", "demo@shop.ma", "demo123", Role.CLIENT));
            dao.create(compte("Admin", "Super", "admin@shop.ma", "admin123", Role.ADMIN));
        });
    }

    private static void ajouter(Vitrine v, Categorie c, String ref, String nom, String desc, String prix, int stock) {
        Produit p = new Produit(ref, nom, desc, new BigDecimal(prix), stock);
        p.setVitrine(v);
        p.setCategorie(c);
        v.getProduits().add(p);
    }

    private static Internaute compte(String nom, String prenom, String email, String mdp, Role role) {
        Internaute i = new Internaute(nom, prenom, email, PasswordUtil.hash(mdp), "Tanger, Maroc");
        i.setProfil(role);
        i.setPanier(new Panier());
        return i;
    }
}
