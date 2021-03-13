package com.example.chatapp;

public class Message {

    private String mssgId ,  mssg , senderId ;
    private String timeStamp ;
    private int feeling = -1;

    public Message() {

    }

    public Message(String mssg, String senderId, String timeStamp) {
        this.mssg = mssg;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public String getMssgId() {
        return mssgId;
    }

    public void setMssgId(String mssgId) {
        this.mssgId = mssgId;
    }

    public String getMssg() {
        return mssg;
    }

    public void setMssg(String mssg) {
        this.mssg = mssg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}
