package com.protect.safetylife.fake_call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.controller.dashboard.LocationMenu;
import com.protect.safetylife.location.TimeHandler;

public class InitializationCall extends AppCompatActivity {

    private Button change;
    private Switch butonOnOff;
    private TextView trackingOnOff;
    private EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization_call);

        butonOnOff=findViewById(R.id.switchFakeCall);
        change=findViewById(R.id.changeNumberFakeCall);
        trackingOnOff=findViewById(R.id.fakeCallActivate);
        number=findViewById(R.id.numberFakeCall);

        InformatieCont.sharedPreferences= getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);

        if(!InformatieCont.sharedPreferences.contains(InformatieCont.phoneFake) || InformatieCont.sharedPreferences.getString(InformatieCont.phoneFake,"")==null)
        {
            SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
            editor.putString(InformatieCont.phoneFake, "0720202029");
            InformatieCont.phoneFake2="0720202029";
            editor.commit();
        }
        number.setText(InformatieCont.sharedPreferences.getString(InformatieCont.phoneFake,""));
        if(InformatieCont.sharedPreferences.contains(InformatieCont.fakeCallOn) && InformatieCont.sharedPreferences.getString(InformatieCont.fakeCallOn,"").equals("on"))
        {
            butonOnOff.setChecked(true);
            trackingOnOff.setText("Fake Call enabled");
            trackingOnOff.setTextColor(Color.GREEN);
            Toast.makeText(this,"Fake Call is enabled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            butonOnOff.setChecked(false);
            trackingOnOff.setText("Fake Call disabled");
            trackingOnOff.setTextColor(Color.RED);
            Toast.makeText(this,"Fake Call is disabled", Toast.LENGTH_SHORT).show();

        }


        butonOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if( ActivityCompat.checkSelfPermission(InitializationCall.this, Manifest.permission.CALL_PHONE ) == PackageManager.PERMISSION_GRANTED) {
                    if (b) {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.fakeCallOn, "on");
                        editor.commit();
                        trackingOnOff.setText("Fake Call enabled");
                        trackingOnOff.setTextColor(Color.GREEN);
                        Toast.makeText(InitializationCall.this,"Fake Call is enabled", Toast.LENGTH_SHORT).show();
                        InformatieCont.fakeCall2=InformatieCont.sharedPreferences.getString(InformatieCont.fakeCallOn,"");
                    } else {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.fakeCallOn, "off");
                        editor.commit();
                        trackingOnOff.setText("Fake Call disabled");
                        trackingOnOff.setTextColor(Color.RED);
                        Toast.makeText(InitializationCall.this,"Fake Call is disabled", Toast.LENGTH_SHORT).show();
                        InformatieCont.fakeCall2=InformatieCont.sharedPreferences.getString(InformatieCont.fakeCallOn,"");
                    }

                }
                else
                {
                    ActivityCompat.requestPermissions(InitializationCall.this,new String[]{Manifest.permission.CALL_PHONE},44);
                    if (b) {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.fakeCallOn, "on");
                        editor.commit();
                        trackingOnOff.setText("Fake Call enabled");
                        trackingOnOff.setTextColor(Color.GREEN);
                        Toast.makeText(InitializationCall.this,"Fake Call is enabled", Toast.LENGTH_SHORT).show();
                        InformatieCont.fakeCall2=InformatieCont.sharedPreferences.getString(InformatieCont.fakeCallOn,"");
                    } else {
                        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                        editor.putString(InformatieCont.fakeCallOn, "off");
                        editor.commit();
                        trackingOnOff.setText("Fake call disabled");
                        trackingOnOff.setTextColor(Color.RED);
                        Toast.makeText(InitializationCall.this,"Fake Call is disabled", Toast.LENGTH_SHORT).show();
                        InformatieCont.fakeCall2=InformatieCont.sharedPreferences.getString(InformatieCont.fakeCallOn,"");
                    }
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                editor.putString(InformatieCont.phoneFake, number.getText().toString());
                InformatieCont.phoneFake2=number.getText().toString();
                editor.commit();
                Toast.makeText(InitializationCall.this,"Number changed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void makePhoneCall()
    {

            Intent intent3 = new Intent(Intent.ACTION_CALL);
            intent3.setData(Uri.parse("tel: 0720869802"));
            startActivity(intent3);

    }

}