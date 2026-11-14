package ma.esith.stockims.web;

import ma.esith.stockims.model.Article;
import ma.esith.stockims.service.ArticleService;
import ma.esith.stockims.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService service;
    private final StockService stock;

    @GetMapping
    public String liste(Model model) {
        List<Article> articles = service.lister();
        Map<Long, Long> stocks = new LinkedHashMap<>();
        for (Article article : articles) {
            stocks.put(article.getId(), stock.calculerStock(article.getId()));
        }
        model.addAttribute("articles", articles);
        model.addAttribute("stocks", stocks);
        return "articles/liste";
    }

    @GetMapping("/nouveau")
    public String formulaire(Model model) {
        model.addAttribute("article", new ArticleForm());
        return "articles/formulaire";
    }

    @PostMapping
    public String ajouter(@Valid @ModelAttribute("article") ArticleForm form,
                          BindingResult erreurs) {
        if (erreurs.hasErrors()) return "articles/formulaire";
        try {
            service.creer(form.getReference(), form.getDesignation());
        } catch (DataIntegrityViolationException e) {
            erreurs.rejectValue("reference", "doublon",
                    "Cette référence existe déjà.");
            return "articles/formulaire";
        }
        return "redirect:/articles";
    }
}
