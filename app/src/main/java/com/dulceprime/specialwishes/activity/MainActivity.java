package com.dulceprime.specialwishes.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.dulceprime.specialwishes.R;
import com.dulceprime.specialwishes.other_components.DBhelper;
import com.dulceprime.specialwishes.other_components.PrefManager;
import com.dulceprime.specialwishes.utils.Tools;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private MainActivity mActivity;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private FragmentHome fragmentHome;
    private FragmentProfile fragmentProfile;
    private FragmentHistory fragmentHistory;
    private SQLiteDatabase db;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = openOrCreateDatabase(DBhelper.DB_NAME, MODE_PRIVATE, null);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            // Insert the birthday messages to database
//            insertBirthdayMessagessToDatabase();
        }

        // STARTING THE SERVICE THAT SENDS THE MESSAGE
//        WE STOP ANY ALREADY STARTED SERVICE FIRST
        stopService(new Intent(getApplicationContext(), com.dulceprime.specialwishes.services.Service_SendingMsg.class));
        startService(new Intent(getApplicationContext(), com.dulceprime.specialwishes.services.Service_SendingMsg.class));


        // Initializing variables
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        fragmentHistory = new FragmentHistory();
        fragmentProfile = new FragmentProfile();
        fragmentHome = new FragmentHome();

        setFragment(fragmentHome); // Setting the home fragment to be default

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        frameLayout = (FrameLayout) findViewById(R.id.main_frame_layout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_home: {
                        setFragment(fragmentHome);
                        return true;
                    }
                    case R.id.bottom_nav_history: {
                        setFragment(fragmentHistory);
                        return true;
                    }
                    case R.id.bottom_nav_profile: {
                        setFragment(fragmentProfile);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

        // checking if the permission has been granted for Android 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }


        initToolbar(); // Initialize the toolbar
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                startActivity(new Intent(MainActivity.this, Settings.class));
                break;
            }
            case R.id.action_about: {
                startActivity(new Intent(MainActivity.this, About.class));
                break;
            }

        }


        return super.onOptionsItemSelected(item);
    }


    // REQUEST PERMISSION
    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.SEND_SMS)
                + ContextCompat.checkSelfPermission(
                mActivity, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, Manifest.permission.SEND_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, Manifest.permission.RECEIVE_BOOT_COMPLETED) || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, Manifest.permission.READ_CONTACTS)) {
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
                builder.setMessage("The permissions enable this app function effectively and accurately.");
                builder.setTitle("Please grant permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                mActivity,
                                new String[]{
                                        Manifest.permission.SEND_SMS,
                                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                        Manifest.permission.READ_CONTACTS
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel", null);
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                Manifest.permission.READ_CONTACTS
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {
            // Do something, when permissions are already granted
//            Toast.makeText(mContext,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                        ) {
                    // Permissions are granted
//                    Toast.makeText(mContext,"Permissions granted.",Toast.LENGTH_SHORT).show();
                } else {
                    // Permissions are denied
                    Toast.makeText(mContext, "Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }





}
