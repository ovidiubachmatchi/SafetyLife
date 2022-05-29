package com.protect.safetylife.recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.controller.powerbutton.ScreenOnOffReceiver;

public class RecorderMenu extends AppCompatActivity {
    private Switch butonOnOff;
    private TextView trackingOnOff;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_menu);

        butonOnOff = findViewById(R.id.switchRecorder);
        trackingOnOff = findViewById(R.id.textTracking);
        spinner = findViewById(R.id.spinner1);
        String [] items = {"30 sec", "60 sec", "90 sec", "120 sec"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        InformatieCont.sharedPreferences= getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        if(InformatieCont.sharedPreferences.contains(InformatieCont.recorder) && InformatieCont.sharedPreferences.getString(InformatieCont.recorder,"").equals("on"))
        {
            butonOnOff.setChecked(true);
            trackingOnOff.setText("Recorder enabled");
            trackingOnOff.setTextColor(Color.GREEN);
            Toast.makeText(this,"Recorder is enabled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            butonOnOff.setChecked(false);
            trackingOnOff.setText("Recorder disabled");
            trackingOnOff.setTextColor(Color.RED);
            Toast.makeText(this,"Recorder is disabled", Toast.LENGTH_SHORT).show();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int x = 0;
                switch (i){
                    case 0:
                        x = 30;
                        break;
                    case 1:
                        x = 60;
                        break;
                    case 2:
                        x = 80;
                        break;
                    case 3:
                        x = 120;
                        break;
                }
                ScreenOnOffReceiver.time = x;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        butonOnOff.setOnCheckedChangeListener((compoundButton, b) -> {
            if (ActivityCompat.checkSelfPermission(RecorderMenu.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                if (b) {
                    SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                    editor.putString(InformatieCont.recorder, "on");
                    editor.commit();
                    trackingOnOff.setText("Recorder enabled");
                    trackingOnOff.setTextColor(Color.GREEN);
                    Toast.makeText(RecorderMenu.this, "Recorder is enabled", Toast.LENGTH_SHORT).show();
                    InformatieCont.recorder2 = InformatieCont.sharedPreferences.getString(InformatieCont.recorder, "");
                } else {
                    SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                    editor.putString(InformatieCont.recorder, "off");
                    editor.commit();
                    trackingOnOff.setText("Recorder disabled");
                    trackingOnOff.setTextColor(Color.RED);
                    Toast.makeText(RecorderMenu.this, "Recorder is disabled", Toast.LENGTH_SHORT).show();
                    InformatieCont.recorder2 = InformatieCont.sharedPreferences.getString(InformatieCont.recorder, "");
                }
            } else {
                ActivityCompat.requestPermissions(RecorderMenu.this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
                if (b) {
                    SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                    editor.putString(InformatieCont.recorder, "on");
                    editor.commit();
                    trackingOnOff.setText("Recorder enabled");
                    trackingOnOff.setTextColor(Color.GREEN);
                    Toast.makeText(RecorderMenu.this, "Recorder is enabled", Toast.LENGTH_SHORT).show();
                    InformatieCont.recorder2 = InformatieCont.sharedPreferences.getString(InformatieCont.recorder, "");
                } else {
                    SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                    editor.putString(InformatieCont.recorder, "off");
                    editor.commit();
                    trackingOnOff.setText("Recorder disabled");
                    trackingOnOff.setTextColor(Color.RED);
                    Toast.makeText(RecorderMenu.this, "Recorder is disabled", Toast.LENGTH_SHORT).show();
                    InformatieCont.recorder2 = InformatieCont.sharedPreferences.getString(InformatieCont.recorder, "");
                }
            }
        });
    }

}