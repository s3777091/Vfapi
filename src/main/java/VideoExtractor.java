import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class VideoExtractor {
    public static void main(String[] args) {
        try {
            String url = "https://arttimes.vn/video/gao-co-can-phai-vo-truoc-khi-nau-khong-c13a36535.html"; // Replace with the actual URL
            Document doc = Jsoup.connect(url).get();

            // Select all article elements with class "art-tlq-bv-items"
            Elements articleElements = doc.select("article.art-tlq-bv-items");

            for (Element articleElement : articleElements) {
                // Extract the video URL
                String videoUrl = articleElement.select("a[href]").attr("href");

                System.out.println("Video URL: " + videoUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
