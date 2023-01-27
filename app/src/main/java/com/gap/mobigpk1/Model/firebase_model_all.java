package com.gap.mobigpk1.Model;

public class firebase_model_all {
    private String title;
    private String link;

    public firebase_model_all(){
    }

    public firebase_model_all(String title, String link) {
        this.title = title;
        this.link = link;
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
