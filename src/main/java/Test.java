import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try {
            String url = "https://www.24h.com.vn/chinh-tri-xa-hoi-c981.html"; // Replace with the actual URL
            Document doc = Jsoup.connect(url).get();

            // Select all article elements within the specified div
            Elements articleElements = doc.select("div.cate-24h-foot-home-latest-list article");

            for (Element articleElement : articleElements) {
                // Extract the title
                String title = articleElement.select("h3.cate-24h-foot-home-latest-list__name a").text();

                // Extract the image URL
                String imageUrl = articleElement.select("figure.cate-24h-foot-home-latest-list__ava a").attr("data-urlimg");

                System.out.println("Title: " + title);
                System.out.println("Image URL: " + imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
