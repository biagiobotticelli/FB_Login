package com.smart_team.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.smart_team.model.SharedPreferencesManager;
import com.smart_team.model.User;
import com.smart_team.smartteam.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private User user;
    private SharedPreferencesManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instance of SharedPreferencesManager
        preferences = new SharedPreferencesManager(this);

        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        // Facebook Login button
        loginButton = (LoginButton) findViewById(R.id.login_button);
        user = new User();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String auth = loginResult.getAccessToken().getToken();
                String id = loginResult.getAccessToken().getUserId();
                String appID = loginResult.getAccessToken().getApplicationId();

                user.setAuthToken(auth);
                user.setID(id);
                user.setAppID(appID);

                preferences.setAuthToken(auth);
                preferences.setID(id);
                preferences.setAppID(appID);

                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String name = profile.getName();
                    String pic = profile.getProfilePictureUri(400,400).toString();

                    user.setName(name);
                    user.setPicture(pic);

                    preferences.setName(name);
                    preferences.setPictureURI(pic);
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String mail = object.getString("email");

                                    user.setEmail(mail);
                                    preferences.setMail(mail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                request.executeAsync();

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                // i.putExtra("authToken", user.getAuthToken());
                startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                Log.d("LoginActivity", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.d("LoginActivity", exception.getCause().toString());
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}