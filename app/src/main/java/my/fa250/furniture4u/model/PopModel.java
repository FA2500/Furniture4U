package my.fa250.furniture4u.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PopModel implements Serializable {

    String ID;
    String description;
    String name;
    double rating;
    double price;
    List<String> img_url;

    String category;
    String type;

    List<String> variance;

    String colour;

    int stock;

    Map<String,Object> varianceList;

    public PopModel()
    {

    }

    public PopModel(String ID, String desc, String name, double rate, double price, List<String> url ,String category, String type, List<String> variance, String colour, int stock, Map<String,Object> varianceList)
    {
        this.ID = ID;
        this.description = desc;
        this.name = name;
        this.rating = rate;
        this.price = price;
        this.img_url = url;
        this.category = category;
        this.type = type;
        this.variance = variance;
        this.colour = colour;
        this.stock = stock;
        this.varianceList = varianceList;
    }

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

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
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

    public List<String> getVariance() {
        return variance;
    }

    public void setVariance(List<String> variance) {
        this.variance = variance;
    }

    public String getColour() { return colour; }

    public void setColour(String colour) { this.colour = colour; }

    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock=stock;}

    public Map<String,Object> getVarianceList() {return varianceList; }

    public void setVarianceList(Map<String,Object> varianceList) {
        this.varianceList = varianceList;
    }
}
