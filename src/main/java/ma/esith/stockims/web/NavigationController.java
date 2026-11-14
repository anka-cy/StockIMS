package ma.esith.stockims.web;

import ma.esith.stockims.model.Article;
import ma.esith.stockims.service.ArticleService;
import ma.esith.stockims.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Gère l'accueil et le choix d'un article avant un mouvement. */
@Controller
@RequiredArgsConstructor
public class NavigationController {
    private final ArticleService articles;
    private final StockService stocks;

    @GetMapping("/")
    public String accueil() {
        return "redirect:/articles";
    }

    @GetMapping("/choix")
    public String choix(@RequestParam(defaultValue = "entree") String operation, Model model) {
        if (!operation.equals("entree") && !operation.equals("sortie")) {
            return "redirect:/articles";
        }
        List<Article> liste = articles.lister();
        Map<Long, Long> valeurs = new LinkedHashMap<>();
        for (Article article : liste) {
            valeurs.put(article.getId(), stocks.calculerStock(article.getId()));
        }
        model.addAttribute("operation", operation);
        model.addAttribute("articles", liste);
        model.addAttribute("stocks", valeurs);
        return "stock/selection";
    }
}
