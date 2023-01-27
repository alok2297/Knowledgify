package com.gap.mobigpk1.Model;

public class ResultsPoll {
    private String postKey;
    private String optionDes;
    private String responseOpt;
    private String total;

    public ResultsPoll(String postKey, String optionDes, String responseOpt, String total) {
        this.postKey = postKey;
        this.optionDes = optionDes;
        this.responseOpt = responseOpt;
        this.total = total;
    }

    public String getResponseOpt() {
        return responseOpt;
    }

    public void setResponseOpt(String responseOpt) {
        this.responseOpt = responseOpt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

}
