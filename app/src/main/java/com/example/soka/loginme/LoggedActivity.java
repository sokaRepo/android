package com.example.soka.loginme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by soka on 19/12/16.
 */

public class LoggedActivity extends Activity{
    private DatabaseController db;
    private TextView helloText;
    TableLayout users_table;
    private String[] columns = {"Name", "Email"};

    private APIReceiver apiReceiver;
    private Intent intentAPI;

    final String APIURL = "http://api.mobile.crashlab.org";


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_logged);

        helloText = (TextView) findViewById(R.id.textViewHello);
        db = new DatabaseController(getApplicationContext());
        String name = db.getSettingByName("name");
        helloText.setText(getString(R.string.welcome_msg) + " " + name);

        // On crée l'en-tête du tableau
        /*
        users_table = (TableLayout)findViewById(R.id.users_table);
        TableRow row0 = new TableRow(this);
        for (String col: columns) {
            TextView tv = new TextView(this);
            tv.setText(col);
            row0.addView(tv);
        }
        users_table.addView(row0);
        TableRow row1 = new TableRow(this);
        TextView tv = new TextView(this);
        tv.setText("soka");
        row1.addView(tv);
        tv.setText("soka@crashlab.org");
        row1.addView(tv);
        */

        /*
        // On crée l'Intent
        apiReceiver = new APIReceiver(new Handler());
        apiReceiver.setReceiver(this);
        intentAPI = new Intent(Intent.ACTION_SYNC, null, this, APIClient.class);
        intentAPI.putExtra("mode", "getusers");
        intentAPI.putExtra("url", APIURL);
        intentAPI.putExtra("receiver", apiReceiver);
        intentAPI.putExtra("requestId", 101);
        */


    }

}
