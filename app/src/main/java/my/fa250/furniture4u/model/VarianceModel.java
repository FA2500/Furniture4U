package my.fa250.furniture4u.model;

import java.io.Serializable;
import java.util.List;

public class VarianceModel implements Serializable {
    String name;
    List<String> img_url;
    double price;
    int stock;
    String url_3d;

    public VarianceModel()
    {

    }

    public VarianceModel(String name, List<String> img_url, double price,int stock,String url_3d) {
        this.name = name;
        this.img_url = img_url;
        this.price = price;
        this.stock = stock;
        this.url_3d = url_3d;
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

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

    public String getUrl_3d() {return url_3d;}

    public void setUrl_3d(String url_3d) { this.url_3d=url_3d; }
}
