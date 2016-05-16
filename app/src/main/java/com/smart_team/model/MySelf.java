package com.smart_team.model;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by biagio on 16/05/16.
 */
public class MySelf extends RealmObject {

    private User user;

    public MySelf(User user){
        this.user = user;
    }

    public MySelf() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}