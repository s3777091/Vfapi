package api.vfapi.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Article {
    private String title;
    private String lead;
    private String url;
    private int publishTime;

}