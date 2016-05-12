package com.smart_team.model;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String ID;

    private String authToken;
    private String appID;
    private String name;
    private String surname;
    private String email;
    private Double latGPS;
    private Double lonGPS;
    private RealmList<Friend> friends;
    private String rest;

    public User(String authToken, String ID, String appID, String name, String surname, String email, Double latGPS, Double lonGPS, RealmList<Friend> friends) {
        this.authToken = authToken;
        this.ID = ID;
        this.appID = appID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.latGPS = latGPS;
        this.lonGPS = lonGPS;
        this.friends = friends;
    }

    public User() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(RealmList<Friend> friends) {
        this.friends = friends;
    }

    public Double getLatGPS() {
        return latGPS;
    }

    public void setLatGPS(Double latGPS) {
        this.latGPS = latGPS;
    }

    public Double getLonGPS() {
        return lonGPS;
    }

    public void setLonGPS(Double lonGPS) {
        this.lonGPS = lonGPS;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }
}