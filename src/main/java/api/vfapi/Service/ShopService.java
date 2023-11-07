package api.vfapi.Service;

import api.vfapi.model.Article;
import api.vfapi.model.Product;

import java.util.List;

public interface ShopService {

    List<Product> fetchProducts(int limit, String token, String category, String url);
}
