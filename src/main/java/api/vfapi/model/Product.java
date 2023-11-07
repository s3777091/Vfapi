package api.vfapi.model;

import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private long id;
    private String name;
    private long price;
    private long listPrice;
    private long discount;
    private double ratingAverage;
    private String thumbnailUrl;

}
