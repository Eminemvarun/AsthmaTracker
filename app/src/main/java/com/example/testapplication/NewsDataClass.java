package com.example.testapplication;

import android.graphics.Bitmap;

public class NewsDataClass {

    private Bitmap image;
    private String imageLink;
    String imageName;
    boolean imageAvailable;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(boolean imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private String title;
    private String link;

    public NewsDataClass(String imageLink, String title, String link,String date) {
        this.imageLink = imageLink;
        this.title = title;
        this.link = link;
        this.imageName = title + "_image";
        this.imageAvailable = false;
        this.date = date;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
