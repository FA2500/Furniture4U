package my.fa250.furniture4u.model;

import java.io.Serializable;

public class InvoiceModel implements Serializable {
    String id;
    Double totalPrice;
    String currentDate;
    String address;
    String productName;
    int totalQuantity;
    String colour;

    public InvoiceModel()
    {

    }

    public InvoiceModel(String id, Double totalPrice, String currentDate, String address, String productName, int totalQuantity, String colour) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.currentDate = currentDate;
        this.address = address;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.colour = colour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
