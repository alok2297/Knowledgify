package com.gap.mobigpk1.Model;

public class Posts {
    String keyPost;
    String profile;
    String name;
    String college;
    String likeCount,commentCount;
    String postType;
    String caption,token;
    String userId;
    String spamReports;

    public Posts(){}

    public Posts(String keyPost, String profile, String name, String college, String likeCount, String commentCount, String postType, String caption, String token, String userId, String spamReports) {
        this.userId = userId;
        this.keyPost = keyPost;
        this.caption = caption;
        this.token = token;
        this.profile = profile;
        this.name = name;
        this.commentCount=commentCount;
        this.college = college;
        this.likeCount = likeCount;
        this.postType = postType;
        this.spamReports = spamReports;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getKeyPost() {
        return keyPost;
    }

    public void setKeyPost(String keyPost) {
        this.keyPost = keyPost;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getSpamReports() {
        return spamReports;
    }

    public void setSpamReports(String spamReports) {
        this.spamReports = spamReports;
    }


}

