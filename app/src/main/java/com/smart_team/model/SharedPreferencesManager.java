package com.smart_team.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    Context context;

    SharedPreferences profile;
    SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context mContext) {
        profile = mContext.getSharedPreferences("profile", 0);
        editor = profile.edit();
    }

    // AuthToken
    public String getAuthToken() {
        return profile.getString("authToken", null);
    }

    public void setAuthToken(String authToken) {
        editor.putString("authToken", authToken);
        editor.commit();
    }

    // User ID
    public String getID() {
        return profile.getString("id", null);
    }

    public void setID(String ID) {
        editor.putString("id", ID);
        editor.commit();
    }

    // App ID
    public String getAppID() {
        return profile.getString("appID", null);
    }

    public void setAppID(String appID) {
        editor.putString("appID", appID);
        editor.commit();
    }

    // Name
    public String getName() {
        return profile.getString("name", null);
    }

    public void setName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    // Email
    public String getMail() {
        return profile.getString("mail", null);
    }

    public void setMail(String mail) {
        editor.putString("mail", mail);
        editor.commit();
    }

    // Picture URI
    public String getPictureURI() {
        return profile.getString("picture", null);
    }

    public void setPictureURI(String picture) {
        editor.putString("picture", picture);
        editor.commit();
    }

}