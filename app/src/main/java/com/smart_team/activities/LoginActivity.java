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
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.Utility;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.smart_team.model.SharedPreferencesManager;
import com.smart_team.model.User;
import com.smart_team.smartteam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.internal.Util;

public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private User user;
    private SharedPreferencesManager preferences;

    String auth,id,appID,name,mail,pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);

        // Instance of SharedPreferencesManager
        preferences = new SharedPreferencesManager(this);

        // Facebook Login button
        loginButton = (LoginButton) findViewById(R.id.login_button);

        // Request Permissions to read data from profile
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        //user = new User();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                auth = loginResult.getAccessToken().getToken();
                appID = loginResult.getAccessToken().getApplicationId();

                preferences.setAuthToken(auth);
                preferences.setAppID(appID);

                Map<String, String> friends = new HashMap<>();

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                final JSONObject jsonObject = response.getJSONObject();
                                try {
                                    id = object.getString("id");
                                    preferences.setID(id);

                                    try {
                                        URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                                        pic = profile_pic.toString();
                                        preferences.setPictureURI(pic);

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    name = object.getString("name");
                                    preferences.setMail(name);

                                    mail = object.getString("email");
                                    preferences.setMail(mail);

                                    JSONObject friends = jsonObject.getJSONObject("friends");
                                    JSONArray data = friends.getJSONArray("data");

                                    // Try with first friend
                                    JSONObject objectdata = data.getJSONObject(0);

                                    friends.put(objectdata.getString("id"), objectdata.getString("name"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email");
                request.setParameters(parameters);
                request.executeAsync();
/*
                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                // Parsare la lista ed aggiungerla in preferences
                                JSONObject json = new JSONObject(response);
                                JSONArray friendsData = json.getJSONArray("data");

                                ArrayList<String> ids = new ArrayList<String>();
                                ArrayList<String> names = new ArrayList<String>();

                                for(int i = 0; i < friendsData.length(); i++){
                                    ids.add(friendsData.getJSONObject(i).getString("id"));
                                    names.add(friendsData.getJSONObject(i).getString("name"));
                                }


                            }
                        }).executeAsync(); */

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
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

        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

}