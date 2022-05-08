package com.protect.safetylife.controller.dashboard;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.location.TimeHadler;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationMenu extends AppCompatActivity {
    private Switch butonOnOff;
    public static Context context;
    private TextView trackingOnOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_menu);
        butonOnOff=findViewById(R.id.switchLocation);
        trackingOnOff=findViewById(R.id.textTracking);
        context=LocationMenu.this;
        InformatieCont.sharedPreferences= getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        if(InformatieCont.sharedPreferences.contains(InformatieCont.locationOn) && InformatieCont.sharedPreferences.getString(InformatieCont.locationOn,"").equals("on"))
        {
            butonOnOff.setChecked(true);
            trackingOnOff.setText("Tracking enabled");
            trackingOnOff.setTextColor(Color.GREEN);
            Toast.makeText(this,"Location tracking is enabled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            butonOnOff.setChecked(false);
            trackingOnOff.setText("Tracking disabled");
            trackingOnOff.setTextColor(Color.RED);
            Toast.makeText(this,"Location tracking is disabled", Toast.LENGTH_SHORT).show();

        }
        butonOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if( ActivityCompat.checkSelfPermission(LocationMenu.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                    if (b) {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.locationOn, "on");
                        editor.commit();
                        TimeHadler time = new TimeHadler(LocationMenu.this);
                        time.cancelTime();
                        time.setTime();
                        trackingOnOff.setText("Tracking enabled");
                        trackingOnOff.setTextColor(Color.GREEN);
                       // Toast.makeText(LocationMenu.this,"Location tracking is enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.locationOn, "off");
                        editor.commit();
                        TimeHadler time = new TimeHadler(LocationMenu.this);
                        time.cancelTime();
                        trackingOnOff.setText("Tracking disabled");
                        trackingOnOff.setTextColor(Color.RED);
                      //  Toast.makeText(LocationMenu.this,"Location tracking is disabled", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    ActivityCompat.requestPermissions(LocationMenu.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                    if (b) {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.locationOn, "on");
                        editor.commit();
                        TimeHadler time = new TimeHadler(LocationMenu.this);
                        time.cancelTime();
                        time.setTime();
                        trackingOnOff.setText("Tracking enabled");
                        trackingOnOff.setTextColor(Color.GREEN);
                       // Toast.makeText(LocationMenu.this,"Location tracking is enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.locationOn, "off");
                        editor.commit();
                        trackingOnOff.setText("Tracking disabled");
                        trackingOnOff.setTextColor(Color.RED);
                       // Toast.makeText(LocationMenu.this,"Location tracking is disabled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}