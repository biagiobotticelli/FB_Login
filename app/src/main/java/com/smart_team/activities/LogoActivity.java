package com.smart_team.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.smart_team.smartteam.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LogoActivity extends Activity {

    AccessTokenTracker accessTokenTracker;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        FacebookSdk.sdkInitialize(getApplicationContext());

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                Realm.setDefaultConfiguration(new RealmConfiguration.Builder(getApplicationContext()).build());
                realm = Realm.getDefaultInstance();
                updateWithToken(newAccessToken);
            }
        };
        updateWithToken(AccessToken.getCurrentAccessToken());

    }

    private void updateWithToken(AccessToken currentAccessToken) {
        // If user is Logged
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(LogoActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);
        }
        // If user is NOT Logged
        else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(LogoActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);
        }
    }
}
