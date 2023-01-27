package com.gap.mobigpk1.Model;

public class Video {

    private String title,content,length,link;

    Video(){}

    public Video(String title, String content, String length, String link) {
        this.title = title;
        this.content = content;
        this.length = length;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
