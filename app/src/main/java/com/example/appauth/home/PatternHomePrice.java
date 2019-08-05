package com.example.appauth.home;

public class PatternHomePrice {

    private  String headline;
    private String desc;
    private  int imageRes;


    public PatternHomePrice(String headline, String desc, int imageRes) {
        this.headline = headline;
        this.desc = desc;
        this.imageRes = imageRes;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDesc() {
        return desc;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
