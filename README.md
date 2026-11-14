# StockIMS — Gestion de stock

Application web réalisée dans le cadre des séances 9 et 10 de Java/P.O.O. à l’ESITH.

## Participants

- **Bennassadia Aymane**
- **Hafid Haytam**

## Fonctionnalités

- Ajouter et consulter des articles.
- Enregistrer une entrée ou une sortie de stock.
- Calculer le stock à partir des mouvements : entrées − sorties.
- Refuser une sortie supérieure au stock disponible.
- Valider côté serveur les champs obligatoires, les références uniques et les quantités positives.
- Afficher l’historique des mouvements d’un article.
- Rechercher un article et demander une confirmation avant une sortie.

## Technologies

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL
- Thymeleaf
- Lombok
- Bootstrap 5.3.8, CSS et JavaScript

## Lancement du projet

### Prérequis

- JDK 21
- MySQL 8

Vérifier la version de Java utilisée par Maven :

```sh
./mvnw -v
```

### Base de données

Créer la base et l’utilisateur MySQL :

```sql
CREATE DATABASE stock_ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'stock_app'@'localhost' IDENTIFIED BY 'stock_app_local';
GRANT ALL PRIVILEGES ON stock_ims.* TO 'stock_app'@'localhost';
```

La connexion se règle dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stock_ims
spring.datasource.username=stock_app
spring.datasource.password=stock_app_local
```

Le mot de passe peut aussi être fourni sans modifier le fichier :

```sh
export DB_PASSWORD='votre_mot_de_passe'
```

Pour charger la structure et les trois articles de test :

```sh
mysql -u root -p < database/stockims.sql
```

Le script SQL recrée les tables `article`, `entree` et `sortie`. Il ne faut pas le réimporter si la base contient des données à conserver.

### Démarrage

Linux/macOS :

```sh
./mvnw spring-boot:run
```

Windows :

```powershell
.\mvnw.cmd spring-boot:run
```

Ouvrir ensuite : **http://localhost:8080/articles**

## Organisation

```text
src/main/java/ma/esith/stockims/
├── model/       Article, MouvementStock, Entree, Sortie
├── repository/  ArticleRepository, EntreeRepository, SortieRepository
├── service/     ArticleService, StockService
└── web/         formulaires et contrôleurs MVC

src/main/resources/
├── templates/   pages Thymeleaf
├── static/      CSS, JavaScript et Bootstrap
└── application.properties
```

`MouvementStock` est une classe abstraite commune aux entrées et aux sorties. La méthode `quantiteSignee()` renvoie une quantité positive pour une entrée et négative pour une sortie. Le stock n’est pas enregistré dans une colonne : il est recalculé à partir des mouvements.

## Personnalisation de l’interface

- **HTML** : navigation entre les articles, les entrées et les sorties, formulaires et tableaux lisibles.
- **CSS / Bootstrap** : couleurs bleu-gris, boutons, messages et affichage adapté aux petits écrans.
- **JavaScript** : filtre de recherche dans la liste des articles et confirmation avant une sortie.

Les validations restent actives lorsque JavaScript est désactivé.

## Données de test

L’export SQL contient trois articles. Pour `PAP-001` :

- entrée de 20 ;
- sortie de 7 ;
- stock obtenu : 13 ;
- une sortie de 15 doit être refusée.

## Captures d’écran

- [Liste des articles](docs/captures/01-inventaire.png)
- [Stock calculé](docs/captures/02-stock-calcule.png)
- [Validation d’une sortie](docs/captures/03-validation.png)
