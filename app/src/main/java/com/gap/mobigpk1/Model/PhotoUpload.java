package com.gap.mobigpk1.Model;

public class PhotoUpload {

    String imgUrl,date,time;
    public PhotoUpload(){}

    public PhotoUpload(String imgUrl, String date, String time) {
        this.imgUrl = imgUrl;
        this.date = date;
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
