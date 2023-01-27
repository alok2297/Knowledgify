package com.gap.mobigpk1.Model;

public class Poll {

    String optionDes;
    String postKey;
    int votes;

    public Poll(String postKey, String optionDes, int votes) {
        this.optionDes = optionDes;
        this.postKey = postKey;
        this.votes = votes;
    }

    public String getOptionDes() {
        return optionDes;
    }

    public void setOptionDes(String optionDes) {
        this.optionDes = optionDes;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
