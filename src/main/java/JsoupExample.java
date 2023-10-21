import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class JsoupExample {
    public static void main(String[] args) {
        try {
            String url = "https://24h.com.vn/tin-tuc-trong-ngay/tin-tuc-24h-qua-bat-chu-tich-hdqt-tap-doan-thai-duong-c46a1511835.html"; // Replace with the actual URL
            Document doc = Jsoup.connect(url).get();

            // Select the main article element
            Element articleElement = doc.selectFirst("article.cate-24h-foot-arti-deta-content-main");

            // Extract the title
            String title = articleElement.select("h1#article_title").text();

            // Extract the content
            Element contentElement = articleElement.select("article.cate-24h-foot-arti-deta-info").first();
            String content = contentElement.text();

            // Extract the image URL
            Element imageElement = contentElement.select("img").first();
            String imageUrl = imageElement.attr("src");

            System.out.println("Title: " + title);
            System.out.println("Content: " + content);
            System.out.println("Image URL: " + imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
