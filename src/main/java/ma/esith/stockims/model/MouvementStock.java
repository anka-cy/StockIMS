package ma.esith.stockims.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Classe abstraite commune à Entree et Sortie.
 * @MappedSuperclass transmet les champs aux tables des sous-classes :
 * aucune table MouvementStock n'est créée.
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MouvementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private LocalDate dateMouvement;

    protected MouvementStock(Article article, int quantite, LocalDate dateMouvement) {
        if (article == null || dateMouvement == null) {
            throw new IllegalArgumentException("Article et date requis.");
        }
        if (quantite <= 0) {
            throw new IllegalArgumentException("Quantité positive requise.");
        }
        this.article = article;
        this.quantite = quantite;
        this.dateMouvement = dateMouvement;
    }

    /** Effet du mouvement sur le stock : positif pour une entrée, négatif pour une sortie. */
    public abstract int quantiteSignee();
}
