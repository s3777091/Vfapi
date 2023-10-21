import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://vnexpress.net/bat-coc-con-trai-cua-nguoi-tinh-4666936.html").get();

            Element articleElement = doc.selectFirst("article.fck_detail");

            for (Element child : articleElement.children()) {
                if ("p".equals(child.tagName()) && "Normal".equals(child.className())) {
                    System.out.println(child.text());
                } else if ("figure".equals(child.tagName())) {
                    String imageUrl = child.selectFirst("img[itemprop=contentUrl]").attr("data-src");
                    System.out.println("Image URL: " + imageUrl);
                }
            }

            Elements authorElements = articleElement.select("p[style=text-align:right]");
            String author = authorElements.size() > 0 ? authorElements.first().text() : "Unknown";
            System.out.println("Author: " + author);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
