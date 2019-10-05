package com.example.r.blogger.Model;



public class Blog {

    public String title;
    public String description;
    public String image;
    public String timeStamp;
    public String userID;


    public Blog(){

    }

    public Blog(String title,String description,String image,String timeStamp,String userID) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.timeStamp = timeStamp;
        this.userID = userID;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

   /* @NonNull
    @Override
    public String toString() {
        return "Blog";

    }*/
}
