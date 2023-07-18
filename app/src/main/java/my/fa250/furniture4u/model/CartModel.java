package my.fa250.furniture4u.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CartModel implements Serializable {

    String id;
    String productID;

    String productCat;
    String currentTime;
    String currentDate;
    String productName;
    double productPrice;
    int totalQuantity;
    double totalPrice;

    List<String> img_url;

    double rating;

    String description;

    Boolean isInCart;

    List<String> variance;
    String colour;

    String url_3d;

    Map<String,Object> varianceList;

    public CartModel()
    {

    }

    public CartModel(String productCat, String productID, String currentTime, String currentDate, String productName, Double productPrice, int totalQuantity, double totalPrice, List<String> img_url, double rating,String description, Boolean isInCart, List<String> variance, String colour, String url_3d ,Map<String,Object> varianceList) {
        this.productID = productID;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.img_url = img_url;
        this.rating = rating;
        this.description = description;
        this.isInCart = isInCart;
        this.variance = variance;
        this.colour = colour;
        this.productCat = productCat;
        this.url_3d = url_3d;
        this.varianceList = varianceList;
    }

    public String getProductCat() {return productCat;}
    public void setProductCat(String productCat) {this.productCat=productCat;}

    public String getProductID() {return productID;}
    public void setProductID(String productID) {this.productID=productID;}

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsInCart() {
        return isInCart;
    }

    public void setIsInCart(Boolean isInCart) {
        this.isInCart = isInCart;
    }

    public List<String> getVariance() {
        return variance;
    }

    public void setVariance(List<String> variance) {
        this.variance = variance;
    }

    public String getColour() { return colour; }

    public void setColour(String colour) { this.colour = colour; }

    public String getUrl_3d() {return url_3d;}

    public void setUrl_3d(String url_3d) {this.url_3d=url_3d;}

    public Map<String,Object> getVarianceList() {return varianceList; }

    public void setVarianceList(Map<String,Object> varianceList) {
        this.varianceList = varianceList;
    }
}
