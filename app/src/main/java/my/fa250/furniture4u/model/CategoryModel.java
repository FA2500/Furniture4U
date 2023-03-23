package my.fa250.furniture4u.model;

public class CategoryModel {

    String img_url;
    String name;
    String type;

    public CategoryModel()
    {

    }

    public CategoryModel(String img_url, String name, String type)
    {
        this.img_url = img_url;
        this.name = name;
        this.type = type;
    }

    public String getImg_url()
    {
        return img_url;
    }

    public void setImg_url(String a)
    {
        this.img_url = a;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String a)
    {
        this.name = a;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String a)
    {
        this.type = a;
    }
}
