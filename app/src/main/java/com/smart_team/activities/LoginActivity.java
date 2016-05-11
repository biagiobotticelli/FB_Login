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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_team.model.Friend;
import com.smart_team.model.User;
import com.smart_team.smartteam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);

        // Facebook Login button
        loginButton = (LoginButton) findViewById(R.id.login_button);

        // Request Permissions to read data from profile
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","user_friends","email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Realm.setDefaultConfiguration(new RealmConfiguration.Builder(getApplicationContext()).build());

                // Get a Realm instance for this thread
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                final User user = realm.createObject(User.class);
                realm.copyToRealm(user);

                user.setAuthToken(loginResult.getAccessToken().getToken());
                user.setAppID(loginResult.getAccessToken().getApplicationId());
                realm.commitTransaction();

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                final JSONObject jsonObject = response.getJSONObject();
                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String mail = object.getString("email");

                                    realm.beginTransaction();
                                    user.setID(id);
                                    user.setName(name);
                                    user.setEmail(mail);

                                    realm.commitTransaction();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email");
                request.setParameters(parameters);
                request.executeAsync();

                GraphRequest friend_request = GraphRequest.newMyFriendsRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray objects, GraphResponse response) {

                                ObjectMapper objectMapper = new ObjectMapper();
                                ;
                                RealmList<Friend> realm_friends = new RealmList<>();
                                realm.copyToRealm(realm_friends);

                                try {
                                    realm.beginTransaction();
                                    List<Friend> friends = objectMapper
                                            .readValue(objects.toString(),
                                                    objectMapper.getTypeFactory().constructCollectionType(List.class, Friend.class));
                                    realm_friends.addAll(friends);
                                    realm.commitTransaction();
                                } catch(IOException e) {
                                    e.printStackTrace();
                                }
                                realm.beginTransaction();
                                user.setFriends(realm_friends);
                                realm.commitTransaction();
                            }
                        });
                Bundle param = new Bundle();
                param.putString("fields", "id, name");
                request.setParameters(param);
                request.executeAsync();

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
    }

}