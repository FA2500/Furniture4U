package my.fa250.furniture4u.model;

public class AddressModel {
    String ID;
    String name ;
    String city ;
    String address ;
    String code ;
    String phone ;
    boolean isPrimary;

    public AddressModel() {
    }

    public AddressModel(String ID,String name, String city, String address, String code, String phone, boolean isPrimary) {
        this.ID = ID;
        this.name = name;
        this.city = city;
        this.address = address;
        this.code = code;
        this.phone = phone;
        this.isPrimary = isPrimary;
    }

    public String getID() {return ID;}

    public void setID(String ID) {this.ID=ID;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
