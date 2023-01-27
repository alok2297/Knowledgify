package com.gap.mobigpk1.Model;

public class ChildItem {

    private String childName , childImage, bookUrl, path;

    public ChildItem(){}

    public ChildItem(String childName, String childImage, String bookUrl, String path) {
        this.childName = childName;
        this.childImage = childImage;
        this.bookUrl = bookUrl;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildImage() {
        return childImage;
    }

    public void setChildImage(String childImage) {
        this.childImage = childImage;
    }
}