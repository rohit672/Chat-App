package com.example.chatapp;

public class Status {

    private String imgUrl;
    private String timeStamp ;

    public Status() {

    }

    public Status(String imgUrl, String timeStamp) {
        this.imgUrl = imgUrl;
        this.timeStamp = timeStamp;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
