package com.protect.safetylife.controller.dashboard;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.location.TimeHandler;

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
                        TimeHandler time = new TimeHandler(LocationMenu.this);
                        time.cancelTime();
                        time.setTime();
                        trackingOnOff.setText("Tracking enabled");
                        trackingOnOff.setTextColor(Color.GREEN);
                       // Toast.makeText(LocationMenu.this,"Location tracking is enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.locationOn, "off");
                        editor.commit();
                        TimeHandler time = new TimeHandler(LocationMenu.this);
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
                        TimeHandler time = new TimeHandler(LocationMenu.this);
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