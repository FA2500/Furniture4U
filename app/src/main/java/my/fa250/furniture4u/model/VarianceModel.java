package my.fa250.furniture4u.model;

import java.io.Serializable;
import java.util.List;

public class VarianceModel implements Serializable {
    String name;
    List<String> img_url;
    double price;

    public VarianceModel()
    {

    }

    public VarianceModel(String name, List<String> img_url, double price) {
        this.name = name;
        this.img_url = img_url;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
