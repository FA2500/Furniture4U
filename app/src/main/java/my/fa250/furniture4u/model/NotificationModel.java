package my.fa250.furniture4u.model;

public class NotificationModel {
    String Content;
    String Title;
    String currentDate;
    String currentTime;

    public NotificationModel()
    {

    }

    public NotificationModel(String content, String title, String currentDate, String currentTime) {
        Content = content;
        Title = title;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
