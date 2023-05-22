package my.fa250.furniture4u.model;

public class SearchModel {
    String img_url;
    String name;
    String ID;



    public SearchModel()
    {

    }

    public SearchModel(String img_url, String name)
    {
        this.img_url = img_url;
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() { return ID; }

    public void setID(String ID) {this.ID = ID;}
}
