package com.envy.asthmatracker;

import android.graphics.Bitmap;

public class NewsDataClass {

    boolean visible = false;
    String imageName;
    boolean imageAvailable;
    String description;
    String date;
    String source;
    private Bitmap image;
    private String imageLink;
    private String title;
    private String link;

    public NewsDataClass(String imageLink, String title, String link, String date, String desc, String source) {
        this.imageLink = imageLink;
        this.title = title;
        this.link = link;
        this.imageName = title.substring(0,15) + "_image";
        this.imageAvailable = false;
        this.date = date;
        this.description = desc;
        this.source = source;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

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
