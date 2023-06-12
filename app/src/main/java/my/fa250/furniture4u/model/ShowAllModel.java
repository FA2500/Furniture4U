package my.fa250.furniture4u.model;

import java.io.Serializable;
import java.util.List;

public class ShowAllModel implements Serializable {

    String ID;
    String description;
    String name;
    double rating;
    double price;
    List<String> img_url;

    List<String> variance;
    String colour;
    //String type;

    public ShowAllModel()
    {

    }

    public ShowAllModel(String ID, String description, String name, double rating, double price, List<String> img_url, String type, List<String> variance, String colour) {
        this.ID = ID;
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.img_url = img_url;
        this.variance = variance;
        this.colour = colour;
        //this.type = type;
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

    /*public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }*/




}
