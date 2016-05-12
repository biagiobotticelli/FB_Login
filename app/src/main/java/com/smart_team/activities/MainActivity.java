package com.smart_team.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.smart_team.model.User;
import com.smart_team.smartteam.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private User user;
    private Realm realm;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        user = realm.where(User.class).findFirst();

        setContentView(R.layout.activity_main);

        TextView auth = (TextView)findViewById(R.id.auth);
        TextView id = (TextView)findViewById(R.id.id);
        TextView appId = (TextView)findViewById(R.id.appId);
        TextView name = (TextView)findViewById(R.id.name);
        TextView surname = (TextView)findViewById(R.id.surname);
        TextView mail = (TextView)findViewById(R.id.mail);
        TextView friend_ID = (TextView)findViewById(R.id.friend_ID);
        TextView friend_name = (TextView)findViewById(R.id.friend_name);
        TextView rest = (TextView)findViewById(R.id.rest);

        auth.setText("authToken = "+user.getAuthToken());
        id.setText("ID = "+user.getID());
        appId.setText("AppID = "+user.getAppID());
        name.setText("Name = "+user.getName());
        surname.setText("Surname = "+user.getSurname());
        mail.setText("Mail = "+user.getEmail());

        if(!user.getFriends().isEmpty()) {
            friend_ID.setText("FRIEND: ID = " + user.getFriends().first().getID());
            friend_name.setText("FRIEND: Name = " + user.getFriends().first().getName());
        }

        rest.setText("REST = "+user.getRest());

        realm.commitTransaction();

        realm.close();

        new HttpRequestTask().execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                final String url = "http://192.168.43.251:8080/beacon";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                String response = restTemplate.getForObject(url, String.class);

                return response;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView restView = (TextView) findViewById(R.id.rest);
            restView.setText(result);
        }
    }

}
