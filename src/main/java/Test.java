import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        try {
            String url = "https://www.24h.com.vn/bong-da-c48.html";
            Document doc = Jsoup.connect(url).get();

            Element articleElement = doc.selectFirst("div.cate-24h-foot-home-latest-list");

            Elements titles = articleElement.select("h3.cate-24h-foot-home-latest-list__name a");
            Elements images = articleElement.select("figure.cate-24h-foot-home-latest-list__ava img");
            Elements descriptions = articleElement.select("div.cate-24h-foot-home-latest-list__sum");
            Elements times = articleElement.select("time.cate-24h-foot-home-latest-list__time");
            Elements links = articleElement.select("h3.cate-24h-foot-home-latest-list__name a[href]");

            for (int i = 0; i < titles.size(); i++) {
                System.out.println("Title: " + titles.get(i).text());
                System.out.println("Image URL: " + images.get(i).attr("data-original"));
                System.out.println("Description: " + descriptions.get(i).text());
                System.out.println("Time: " + times.get(i).text());
                System.out.println("Link: " + links.get(i).attr("href"));
                System.out.println("--------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
