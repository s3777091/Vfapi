package api.vfapi.Service.Implement;


import api.vfapi.Service.HomeService;
import api.vfapi.model.Article;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static api.vfapi.config.constant.KINH_DOANH;
import static api.vfapi.config.constant.VN_NEWS;

@Service
public class HomeImplement implements HomeService {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

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
                    return article;
                })
                .collect(Collectors.toList());
    }

    public List<Article> scrapeUrls(List<String> urls) {
        List<Article> allArticles = new ArrayList<>();
        List<Future<List<Article>>> futures = new ArrayList<>();

        for (String url : urls) {
            if (url != null) {  // Check if url is not null before submitting to executor
                futures.add(executor.submit(() -> scrapeSingleUrl(url)));
            }
        }

        for (Future<List<Article>> future : futures) {
            try {
                allArticles.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return allArticles;
    }

    private List<Article> scrapeSingleUrl(String url) {
        List<Article> list = new ArrayList<>();
        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(10 * 1000)  // Set timeout to 10 seconds
                        .get();

                Element articleElement = doc.selectFirst("div.cate-24h-foot-home-latest-list");
                if (articleElement == null) {
                    System.out.println("Couldn't find the expected article element for URL: " + url);
                    return list;
                }

                Elements titles = articleElement.select("h3.cate-24h-foot-home-latest-list__name a");
                Elements images = articleElement.select("figure.cate-24h-foot-home-latest-list__ava img");
                Elements descriptions = articleElement.select("div.cate-24h-foot-home-latest-list__sum");
                Elements times = articleElement.select("time.cate-24h-foot-home-latest-list__time");
                Elements links = articleElement.select("h3.cate-24h-foot-home-latest-list__name a[href]");

                for (int i = 0; i < titles.size(); i++) {
                    Article ts = new Article();
                    ts.setTitle(titles.get(i).text());
                    ts.setImage(images.get(i).attr("data-original"));
                    ts.setLead(descriptions.get(i).text());
                    ts.setPublishTime(times.get(i).text());
                    ts.setUrl(links.get(i).attr("href"));
                    list.add(ts);
                }

                break;

            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 503 && retryCount < maxRetries) {
                    retryCount++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                } else {
                    System.err.println("An error occurred while scraping URL: " + url);
                    e.printStackTrace();
                    break;
                }
            } catch (Exception e) {
                System.err.println("An error occurred while scraping URL: " + url);
                e.printStackTrace();
                break;
            }
        }

        return list;
    }

}
