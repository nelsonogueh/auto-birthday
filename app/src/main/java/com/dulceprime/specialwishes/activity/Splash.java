package com.dulceprime.specialwishes.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dulceprime.specialwishes.R;
import com.dulceprime.specialwishes.other_components.DBhelper;
import com.dulceprime.specialwishes.other_components.PrefManager;

public class Splash extends AppCompatActivity {

    private PrefManager prefManager;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        db = openOrCreateDatabase(DBhelper.DB_NAME, MODE_PRIVATE, null);

        // Checking for first time launch  and create all the tables
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            DBhelper dBhelper = new DBhelper(getApplicationContext());
            dBhelper.onCreate(db);
        }

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(6000);  // wait for 6 seconds before you do the following things
                    Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }; // ends the Thread

        // calling the thread
        myThread.start();
    }
}
