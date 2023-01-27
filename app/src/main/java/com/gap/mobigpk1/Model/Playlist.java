package com.gap.mobigpk1.Model;

public class Playlist {
    String title,link,time;

    Playlist(){}

    public Playlist(String title, String link ,String time) {
        this.title = title;
        this.link = link;

        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
