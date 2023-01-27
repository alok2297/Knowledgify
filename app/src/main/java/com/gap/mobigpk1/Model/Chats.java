package com.gap.mobigpk1.Model;

public class Chats {

    String teacherName;
    String lastMesssage,key,stChatKey;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Chats(String teacherName, String lastMesssage, String profile, String key,String stChatKey) {
        this.teacherName = teacherName;
        this.lastMesssage = lastMesssage;
        this.profile = profile;
        this.key=key;
        this.stChatKey=stChatKey;
    }

    public String getStChatKey() {
        return stChatKey;
    }

    public void setStChatKey(String stChatKey) {
        this.stChatKey = stChatKey;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLastMesssage() {
        return lastMesssage;
    }

    public void setLastMesssage(String lastMesssage) {
        this.lastMesssage = lastMesssage;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    String profile;

    public Chats(){}


}
