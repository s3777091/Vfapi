package api.vfapi.Controller;

import api.vfapi.Service.HomeService;
import api.vfapi.Service.ShopService;
import api.vfapi.model.Article;
import api.vfapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private HomeService homeService;


    @Autowired
    private ShopService shopService;


    @GetMapping("/articles")
    public List<Article> getArticles() {
        return homeService.fetchArticles();
    }

    @GetMapping("/articles/24h")
    public List<Article> get24H() {

        List<String> urls = Arrays.asList(
                "https://www.24h.com.vn/tin-tuc-trong-ngay-c46.html",
                "https://www.24h.com.vn/tin-tuc-quoc-te-c415.html",
                "https://www.24h.com.vn/kinh-doanh-c161.html",
                "https://www.24h.com.vn/an-ninh-hinh-su-c51.html",
                "https://www.24h.com.vn/nong-tren-mang-c983.html",
                "https://www.24h.com.vn/thi-truong-tieu-dung-c52.html",
                "https://www.24h.com.vn/thoi-trang-c78.html",
                "https://www.24h.com.vn/bong-da-c48.html",
                "https://www.24h.com.vn/giai-tri-c731.html"

        );

        return homeService.scrapeUrls(urls);
    }


    //Token value cbb1ae28-cd12-b391-ad41-b4e898678566
    //Category 1789
    //Url dien-thoai-may-tinh-bang


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProductsTiki(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "token", required = false, defaultValue = "cbb1ae28-cd12-b391-ad41-b4e898678566") String token,
            @RequestParam(value = "category", required = false, defaultValue = "1789") String category,
            @RequestParam(value = "url", required = false, defaultValue = "dien-thoai-may-tinh-bang") String url) {
        List<Product> products = shopService.fetchProducts(limit, token, category, url);
        return ResponseEntity.ok(products);
    }


}
