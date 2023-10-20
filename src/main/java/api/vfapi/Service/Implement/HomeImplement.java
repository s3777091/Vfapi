package api.vfapi.Service.Implement;


import api.vfapi.Service.HomeService;
import api.vfapi.model.Article;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static api.vfapi.config.constant.VN_NEWS;

@Service
public class HomeImplement implements HomeService {

    public List<Article> fetchArticles() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VN_NEWS))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parse)
                .join();
    }

    private List<Article> parse(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        int code = json.getInt("code");
        if (code != 200) {
            return Collections.emptyList();
        }

        JSONObject data = json.getJSONObject("data");
        return data.keySet().parallelStream()
                .flatMap(key -> StreamSupport.stream(data.getJSONObject(key).getJSONArray("data").spliterator(), true))
                .map(obj -> (JSONObject) obj)
                .map(articleJson -> {
                    Article article = new Article();
                    article.setTitle(articleJson.getString("title"));
                    article.setLead(articleJson.getString("lead"));
                    article.setUrl(articleJson.getString("share_url"));
                    article.setPublishTime(articleJson.getInt("publish_time"));
                    return article;
                })
                .collect(Collectors.toList());
    }

}
