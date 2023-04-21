package my.fa250.furniture4u.model;

import java.io.Serializable;

public class CartModel implements Serializable {

    String currentTime;
    String currentDate;
    String productName;
    double productPrice;
    int totalQuantity;
    double totalPrice;

    String img_url;

    double rating;

    String description;

    Boolean isInCart;

    public CartModel()
    {

    }

    public CartModel(String currentTime, String currentDate, String productName, Double productPrice, int totalQuantity, double totalPrice, String img_url, double rating,String description, Boolean isInCart) {
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.img_url = img_url;
        this.rating = rating;
        this.description = description ;
        this.isInCart = isInCart;
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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
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
}
