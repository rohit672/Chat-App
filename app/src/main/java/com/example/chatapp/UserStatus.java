package com.example.chatapp;

import java.util.ArrayList;

public class UserStatus {

    private String name , profileImg ;
    private String lastUpdated;
    ArrayList<Status> statuses ;

    public UserStatus() {

    }

    public UserStatus(String name, String profileImg, String lastUpdated, ArrayList<Status> statuses) {
        this.name = name;
        this.profileImg = profileImg;
        this.lastUpdated = lastUpdated;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
