package com.gap.mobigpk1.Model;

public class ChatModel {
    String message;
    String from;

    public ChatModel() {
    }

    public ChatModel(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
