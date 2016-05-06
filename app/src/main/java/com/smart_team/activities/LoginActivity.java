package com.smart_team.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.smart_team.smartteam.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private String id, name, email, birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Facebook Login button
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String token = loginResult.getAccessToken().getToken();
                String id = loginResult.getAccessToken().getUserId();
                String appid = loginResult.getAccessToken().getApplicationId();


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("token", token);
                i.putExtra("id", id);
                i.putExtra("appid", appid);
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
        // try do comment on 81
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}