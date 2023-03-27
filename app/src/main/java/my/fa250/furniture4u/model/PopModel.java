package my.fa250.furniture4u.model;

public class PopModel {

    String description;
    String name;
    double rating;
    double price;
    String img_url;

    public PopModel()
    {

    }

    public PopModel(String desc, String name, double rate, double price, String url)
    {
        this.description = desc;
        this.name = name;
        this.rating = rate;
        this.price = price;
        this.img_url = url;
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
}
