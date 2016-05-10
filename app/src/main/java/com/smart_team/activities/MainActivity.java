package com.smart_team.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smart_team.model.SharedPreferencesManager;
import com.smart_team.model.User;
import com.smart_team.smartteam.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    //private Realm realm;
    private SharedPreferencesManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instance of SharedPreferencesManager
        preferences = new SharedPreferencesManager(this);

        /*
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        */

        setContentView(R.layout.activity_main);

        TextView auth = (TextView)findViewById(R.id.auth);
        TextView id = (TextView)findViewById(R.id.id);
        TextView appId = (TextView)findViewById(R.id.appId);
        TextView name = (TextView)findViewById(R.id.name);
        TextView mail = (TextView)findViewById(R.id.mail);

        /*
        Intent i = getIntent();
        String authToken = i.getStringExtra("authToken");
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("authToken", authToken).findFirst();
        */

        auth.setText("authToken = "+preferences.getAuthToken());
        id.setText("ID = "+preferences.getID());
        appId.setText("AppID = "+preferences.getAppID());
        name.setText("Name = "+preferences.getName());
        mail.setText("Mail = "+preferences.getMail());

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();
        try {
            friendslist = new JSONArray(jsondata);
            for (int l=0; l < friendslist.length(); l++) {
                friends.add(friendslist.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //realm.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //realm.close();
    }
}
