package com.gap.mobigpk1.Model;

public class Update {

    String title,coverImg,image,key;
    public Update() {}

    public Update(String title, String coverImg, String image, String key) {
        this.title = title;
        this.coverImg = coverImg;
        this.image = image;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
