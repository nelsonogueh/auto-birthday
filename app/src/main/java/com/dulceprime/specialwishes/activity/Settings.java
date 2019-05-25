package com.dulceprime.specialwishes.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;



import android.graphics.Color;

import com.dulceprime.specialwishes.R;

public class Settings extends AppCompatActivity {

    private Switch themeSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitcher = (Switch) findViewById(R.id.setting_theme_switch);
        themeSwitcher.isChecked();
        themeSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(Settings.this, "Checked", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Settings.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
