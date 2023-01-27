package com.gap.mobigpk1.Model;

public class Notification {
    String commenterName,commentOnPost,postKey,notifyKey;

    public Notification(){}

    public Notification(String commenterName, String commentOnPost, String postKey, String notifyKey) {
        this.commenterName = commenterName;
        this.commentOnPost = commentOnPost;
        this.postKey = postKey;
        this.notifyKey = notifyKey;
    }

    public String getNotifyKey() {
        return notifyKey;
    }

    public void setNotifyKey(String notifyKey) {
        this.notifyKey = notifyKey;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommentOnPost() {
        return commentOnPost;
    }

    public void setCommentOnPost(String commentOnPost) {
        this.commentOnPost = commentOnPost;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
