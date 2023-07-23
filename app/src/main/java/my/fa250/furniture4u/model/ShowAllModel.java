package my.fa250.furniture4u.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ShowAllModel implements Serializable {

    String ID;
    String description;
    String name;
    double rating;
    double price;
    List<String> img_url;
    List<String> variance;
    String colour;
    String category;
    String url_3d;
    String type;
    int stock;
    Map<String,Object> varianceList;

    public ShowAllModel()
    {

    }

    public ShowAllModel(String category, String ID, String description, String name, double rating, double price, List<String> img_url, String type, List<String> variance, String colour, String url_3d, int stock, Map<String,Object> varianceList) {
        this.ID = ID;
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.img_url = img_url;
        this.variance = variance;
        this.colour = colour;
        this.category = category;
        this.url_3d = url_3d;
        this.type = type;
        this.stock = stock;
        this.varianceList = varianceList;
    }

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category=category;}
    public String getID() {return ID;}
    public void setID(String ID) {this.ID = ID;}
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
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public List<String> getImg_url() {
        return img_url;
    }
    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }
    public List<String> getVariance() {
        return variance;
    }
    public void setVariance(List<String> variance) {
        this.variance = variance;
    }
    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl_3d() {return url_3d;}
    public void setUrl_3d(String url_3d) {this.url_3d=url_3d;}
    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock=stock;}
    public Map<String,Object> getVarianceList() {return varianceList; }
    public void setVarianceList(Map<String,Object> varianceList) {
        this.varianceList = varianceList;
    }


}
