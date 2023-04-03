package my.fa250.furniture4u.model;

import java.io.Serializable;

public class ProductModel implements Serializable {

    String description;
    String name;
    double rating;
    double price;
    String img_url;

    String category;
    String type;

    public ProductModel()
    {

    }

    public ProductModel(String desc, String name, double rate, double price, String url, String category, String type)
    {
        this.description = desc;
        this.name = name;
        this.rating = rate;
        this.price = price;
        this.img_url = url;
        this.category = category;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
