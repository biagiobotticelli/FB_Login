package com.smart_team.model;

import java.util.Map;

public class User {

    private String authToken;
    private String ID;
    private String appID;
    private String name;
    private String email;
    private String picture;
    private Map<String,String> friends;

    public User(String authToken, String ID, String appID, String name, String email, String picture) {
        this.authToken = authToken;
        this.ID = ID;
        this.appID = appID;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public User() {
        this.authToken = "";
        this.ID = "";
        this.appID = "";
        this.name = "";
        this.email = "";
        this.picture = "";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Map<String, String> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, String> friends) {
        this.friends = friends;
    }

}