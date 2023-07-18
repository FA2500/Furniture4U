package my.fa250.furniture4u.model;

public class AddressModel {
    String ID;
    String name ;
    String phone ;

    String address ;

    String code ;
    String district;
    String state ;
    boolean isPrimary;

    public AddressModel() {
    }

    public AddressModel(String ID,String name, String city, String address, String code, String phone, boolean isPrimary,String district,String state) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.code = code;
        this.phone = phone;
        this.isPrimary = isPrimary;
        this.district = district;
        this.state = state;
    }

    public String getID() {return ID;}

    public void setID(String ID) {this.ID=ID;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean getisPrimary() {
        return isPrimary;
    }

    public void setisPrimary(boolean primary) {
        isPrimary = primary;
    }
}
