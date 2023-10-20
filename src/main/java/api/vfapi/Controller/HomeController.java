package api.vfapi.Controller;

import api.vfapi.Service.HomeService;
import api.vfapi.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return homeService.fetchArticles();
    }
}
