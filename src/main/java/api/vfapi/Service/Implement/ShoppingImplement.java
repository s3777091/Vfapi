package api.vfapi.Service.Implement;

import api.vfapi.Service.ShopService;
import api.vfapi.model.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static api.vfapi.config.constant.TIKI_SLUG;

@Service
public class ShoppingImplement implements ShopService {


    @Override
    public List<Product> fetchProducts(int limit, String token, String category, String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TIKI_SLUG
                        .concat("?limit=" + limit + "&trackity_id=" + token + "&category=" + category + "&urlKey="+ url)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parse)
                .join();
    }

    private List<Product> parse(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        JSONArray productsJsonArray = json.getJSONArray("data"); // Directly get the JSONArray

        return IntStream.range(0, productsJsonArray.length())
                .mapToObj(productsJsonArray::getJSONObject) // Get each JSONObject in the array
                .map(productJson -> {
                    Product product = new Product();
                    product.setId(productJson.getLong("id"));
                    product.setName(productJson.getString("name"));
                    product.setPrice(productJson.getLong("price"));
                    product.setListPrice(productJson.has("list_price") ? productJson.getLong("list_price") : 0); // Check if key exists
                    product.setDiscount(productJson.getLong("discount"));
                    product.setRatingAverage(productJson.getDouble("rating_average"));
                    product.setThumbnailUrl(productJson.getString("thumbnail_url"));
                    return product;
                })
                .collect(Collectors.toList());
    }



}
