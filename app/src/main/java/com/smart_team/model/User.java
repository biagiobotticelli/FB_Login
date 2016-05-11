package com.smart_team.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String ID;

    private String authToken;
    private String appID;
    private String name;
    private String email;
    private RealmList<Friend> friends;

    public User(String authToken, String ID, String appID, String name, String email, RealmList<Friend> friends) {
        this.authToken = authToken;
        this.ID = ID;
        this.appID = appID;
        this.name = name;
        this.email = email;
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

}