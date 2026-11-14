-- StockIMS — structure MySQL 8 et jeu de données de test.
-- ATTENTION : ce script RECRÉE les trois tables du TP ; il efface les mouvements
-- et les articles déjà enregistrés. Utiliser une base de test, ou faire une
-- sauvegarde avant : mysqldump --no-tablespaces -u root -p stock_ims > sauvegarde.sql
-- Exemple : mysql -u root -p < database/stockims.sql
CREATE DATABASE IF NOT EXISTS stock_ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stock_ims;

DROP TABLE IF EXISTS sortie;
DROP TABLE IF EXISTS entree;
DROP TABLE IF EXISTS article;

CREATE TABLE article (
    id BIGINT NOT NULL AUTO_INCREMENT,
    reference VARCHAR(20) NOT NULL,
    designation VARCHAR(80) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_article_reference UNIQUE (reference)
) ENGINE=InnoDB;

CREATE TABLE entree (
    id BIGINT NOT NULL AUTO_INCREMENT,
    article_id BIGINT NOT NULL,
    quantite INT NOT NULL,
    date_mouvement DATE NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_entree_article (article_id),
    CONSTRAINT fk_entree_article FOREIGN KEY (article_id) REFERENCES article(id),
    CONSTRAINT ck_entree_quantite CHECK (quantite > 0)
) ENGINE=InnoDB;

CREATE TABLE sortie (
    id BIGINT NOT NULL AUTO_INCREMENT,
    article_id BIGINT NOT NULL,
    quantite INT NOT NULL,
    date_mouvement DATE NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_sortie_article (article_id),
    CONSTRAINT fk_sortie_article FOREIGN KEY (article_id) REFERENCES article(id),
    CONSTRAINT ck_sortie_quantite CHECK (quantite > 0)
) ENGINE=InnoDB;

START TRANSACTION;
INSERT INTO article (id, reference, designation) VALUES
    (1, 'PAP-001', 'Carnet A5 — papier recyclé'),
    (2, 'BUR-002', 'Stylo à bille — encre noire'),
    (3, 'ACC-003', 'Pochette de rangement — format A4');
INSERT INTO entree (id, article_id, quantite, date_mouvement) VALUES (1, 1, 20, '2026-10-20');
INSERT INTO sortie (id, article_id, quantite, date_mouvement) VALUES (1, 1, 7, '2026-10-21');
COMMIT;

-- Stock final attendu : PAP-001 = 13 ; BUR-002 = 0 ; ACC-003 = 0.
-- Les sommes sont agrégées séparément pour éviter de multiplier les mouvements
-- lors des jointures (un article peut avoir plusieurs entrées ET sorties).
SELECT a.reference, a.designation,
       COALESCE(e.total, 0) - COALESCE(s.total, 0) AS stock
FROM article a
LEFT JOIN (SELECT article_id, SUM(quantite) total FROM entree GROUP BY article_id) e ON e.article_id = a.id
LEFT JOIN (SELECT article_id, SUM(quantite) total FROM sortie GROUP BY article_id) s ON s.article_id = a.id
ORDER BY a.reference;
