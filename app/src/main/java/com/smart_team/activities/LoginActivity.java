package com.smart_team.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.smart_team.model.Friend;
import com.smart_team.model.User;
import com.smart_team.model.MySelf;
import com.smart_team.rest.AuthOrSignupUser;
import com.smart_team.smartteam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private Realm realm;
    private User user;
    private String authToken;
    private String serverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);

        // Facebook Login button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        // Request Permissions to read data from profile
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","user_friends","email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try {
                                    // Data received from server
                                    String token = loginResult.getAccessToken().getToken();
                                    String id = object.getString("id");
                                    String name = object.getString("first_name");
                                    String surname = object.getString("last_name");
                                    String mail = object.getString("email");

                                    Log.d("LoginTask", token + " | " + id + " | " + name
                                            + " | " + surname + " | " + mail);

                                    User requestUser = new User();
                                    requestUser.setAuthToken(token);
                                    requestUser.setFacebookID(id);
                                    requestUser.setName(name);
                                    requestUser.setSurname(surname);
                                    requestUser.setEmail(mail);

                                    User responseUser = new AuthOrSignupUser().execute(requestUser).get();
                                    Log.d("LoginTask","Returned from rest call: "+ responseUser.toString());
                                    realm.beginTransaction();
                                    MySelf myself = realm.createObject(MySelf.class);
                                    User realmResponseUser = realm.copyToRealm(responseUser);
                                    myself.setUser(realmResponseUser);
                                    realm.commitTransaction();

                                    // See Singleton implementation of GroupTracking

                                    //Reference to usedId in the WorkflowManager, useful for rest calls
                                    // WorkflowManager.getWorkflowManager().setMyselfId(responseUser.getUid());

                                } catch (JSONException | InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();

                realm.beginTransaction();
                final RealmList<Friend> realm_friends = new RealmList<>();
                realm.copyToRealm(realm_friends);
                realm.commitTransaction();

                GraphRequest friend_request = GraphRequest.newMyFriendsRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray objects, GraphResponse response) {
                                Log.i("JSONArray", objects.toString());

                                for (int i = 0; i < objects.length(); i++) {
                                    try {
                                        JSONObject friend = objects.getJSONObject(i);
                                        String id = friend.getString("id");
                                        String name = friend.getString("first_name");
                                        String surname = friend.getString("last_name");

                                        realm.beginTransaction();
                                        Friend realm_friend = realm.createObject(Friend.class);
                                        realm_friend.setID(id);
                                        realm_friend.setName(name);
                                        realm_friend.setSurname(surname);
                                        realm_friends.add(realm_friend);
                                        realm.commitTransaction();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                realm.beginTransaction();
                                user.setFriends(realm_friends);
                                realm.commitTransaction();
                            }
                        });
                Bundle param = new Bundle();
                param.putString("fields", "id, first_name, last_name");
                friend_request.setParameters(param);
                friend_request.executeAsync();

                //User userFromServer = new LoginSignUp();

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

    private class LoginSignUp extends AsyncTask<User, Void, User> {
        @Override
        protected User doInBackground(User... params) {
            try {

                User user = params[0];
                Log.d("Rest","GET on /user. Input user for rest call is: "+user.toString());

                final String url = "http://amaca.ga:8080/user?token=" + authToken+
                        "&facebookId="+user.getFacebookID()+"&name="+user.getName()+
                        "&surname="+user.getSurname()+"&email="+user.getEmail();

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                User userFromServer = restTemplate.getForObject(url, User.class);
                Log.d("Rest", userFromServer.toString());
                return userFromServer;

            } catch (Exception e) {
                Log.e("Rest", e.getMessage(), e);
                return null;
            }
        }

    }

}