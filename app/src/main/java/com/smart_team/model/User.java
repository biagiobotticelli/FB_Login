package com.smart_team.model;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String serverID;
    private String facebookID;
    private String authToken;
    private String appID;
    private String name;
    private String surname;
    private String email;
    private Double latGPS;
    private Double lonGPS;
    private RealmList<Friend> friends;
    private String rest;

    public User(String serverID, String authToken, String facebookID, String appID, String name, String surname, String email, Double latGPS, Double lonGPS, RealmList<Friend> friends) {
        this.serverID = serverID;
        this.authToken = authToken;
        this.facebookID = facebookID;
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

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String ID) {
        this.facebookID = facebookID;
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

    @Override
    public String toString() {
        String s = "LOGGED USER \n";
        s += "Server ID = \t\t"+ this.getServerID();
        s += "Facebook ID = \t\t"+ this.getFacebookID();
        s += "Email = \t\t"+ this.getEmail();
        s += "Name = \t\t"+ this.getName();
        s += "Surname = \t\t"+ this.getSurname()+ "\n";

        return s;
    }

}