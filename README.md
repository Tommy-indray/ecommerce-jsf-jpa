# E-Shop : application web JSF + JPA

Site e-commerce simplifie : vitrine, internautes, panier, commandes.
Stack : Java 8+, Maven, Tomcat 9, JSF 2.2 (Mojarra), JPA 2.2 (EclipseLink), MySQL.

## Prerequis
- JDK 8 ou plus, Maven 3.6+
- MySQL 5.7/8 demarre sur localhost:3306 (utilisateur `root`, mot de passe vide par defaut)
- **Tomcat 9** (espace de noms `javax.*`). Tomcat 10+ n'est PAS compatible avec ce projet.

## Configuration
Adapter si besoin `src/main/resources/META-INF/persistence.xml` (url, user, password).
La base `ecommerce_db` et toutes les tables sont creees automatiquement au demarrage
(`createDatabaseIfNotExist=true` + `eclipselink.ddl-generation`). Le script `sql/create_database.sql`
permet de creer la base a la main.

## Lancement
Avec Eclipse : File > Import > Existing Maven Projects, puis Run on Server (Tomcat 9).
En ligne de commande : `mvn clean package` puis copier `target/ecommerce.war` dans `TOMCAT/webapps`.

URL : http://localhost:8080/ecommerce/

## Comptes de demonstration
- Client : demo@shop.ma / demo123
- Admin  : admin@shop.ma / admin123 (menu Administration : ajout / suppression de produits)
