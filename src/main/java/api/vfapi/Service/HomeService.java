package api.vfapi.Service;

import api.vfapi.model.Article;

import java.util.List;

public interface HomeService {
    List<Article> fetchArticles();
    List<Article> scrapeUrls(List<String> urls);
}
