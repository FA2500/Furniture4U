package my.fa250.furniture4u.model;

public class ProfileModel {
    int srcID;
    String title;
    int noOfNotif;

    public ProfileModel()
    {

    }

    public ProfileModel(int src, String title, int noOfNotif) {
        this.srcID = src;
        this.title = title;
        this.noOfNotif = noOfNotif;
    }

    public int getSrcID() {
        return srcID;
    }

    public void setSrcID(int src) {
        this.srcID = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoOfNotif() {
        return noOfNotif;
    }

    public void setNoOfNotif(int noOfNotif) {
        this.noOfNotif = noOfNotif;
    }
}
