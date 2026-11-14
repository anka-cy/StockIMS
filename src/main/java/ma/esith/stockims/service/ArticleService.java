package ma.esith.stockims.service;

import ma.esith.stockims.model.Article;
import ma.esith.stockims.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;

    public List<Article> lister() {
        return repository.findAll();
    }

    public Article creer(String reference, String designation) {
        return repository.save(new Article(reference, designation));
    }
}
