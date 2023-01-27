package com.gap.mobigpk1.Model;

public class Comments {
    String commentKey;
    String name;
    String college;
    String type;
    String textComment;
    String picComment;

    public Comments(){}

    public Comments(String commentKey,String name, String type, String textComment) {
        this.name = name;
        this.commentKey = commentKey;
        this.type = type;
        this.textComment = textComment;
        this.picComment = picComment;
    }


    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }

    public String getPicComment() {
        return picComment;
    }

    public void setPicComment(String picComment) {
        this.picComment = picComment;
    }
}
