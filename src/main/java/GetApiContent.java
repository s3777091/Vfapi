import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetApiContent {
    public static void main(String[] args) throws IOException {
        String url = "https://www.24h.com.vn/the-thao/djokovic-huong-toi-viec-vuot-federer-connors-de-gianh-hon-109-danh-hieu-atp-c101a1521560.html";
        Document document = Jsoup.connect(url).get();

        String content = document.body().text();
        System.out.println(content);

        Element element = document.select(".cate-24h-foot-main *").first(); 
        String cont = element.text();
        System.out.println(cont);
    }
}