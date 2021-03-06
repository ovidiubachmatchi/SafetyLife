package com.protect.safetylife.controller.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.LandingActivity;
import com.protect.safetylife.controller.powerbutton.ScreenOnOffBackgroundService;
import com.protect.safetylife.controller.safetytime.SafetyTimeActivity;

import com.protect.safetylife.fake_call.InitializationCall;
import com.protect.safetylife.recorder.Recorder;
import com.protect.safetylife.recorder.RecorderMenu;

import java.io.File;
import java.util.Calendar;

import com.protect.safetylife.controller.stealprotection.CameraService;
import com.protect.safetylife.controller.stealprotection.StealActivity;

/**
    Main activity
*/
public class DashboardActivity extends AppCompatActivity {

    private TextView mesajLogat;
    private ProgressDialog progres;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static Context context;

    private ImageView sosBtn;
    private ImageView watchBtn;
    private ImageView locationBtn;
    private ImageView cameraBtn;
    private ImageView stealBtn;
    private ImageView fakeCallBtn;
    private ImageView settings;
    public static String pathRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting visual layout
        setContentView(R.layout.menu);
        // starting background service
        context = this;
        System.out.println("---------------" + "context Dashboard:" + context);
        Intent backgroundService = new Intent(this, ScreenOnOffBackgroundService.class);


        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            requestPermissions();
        }
        if (!isMyServiceRunning(ScreenOnOffBackgroundService.class))
            startService(backgroundService);

        // if the activity was opened from the notification background service
        // we are going to show the force close button
        Intent intent = getIntent();
        if (intent != null && "intentForceClose".equals(intent.getAction())) {
            showForceCloseButton();
            Toast.makeText(this,
                    "apasat din notificare, aplicatia era inchisa",
                    Toast.LENGTH_LONG).show();
        }

        /**
         * Initializam si verificam logarea
         * la initializare se colecteaza datele din fisiere stocate se face privat
         */
        InformatieCont.sharedPreferences = getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        mesajLogat = findViewById(R.id.mesajLogat);
        if (InformatieCont.verificareLogat()) {
            String mesaj = "";
            if (InformatieCont.username2 == null)
                mesaj = InformatieCont.sharedPreferences.getString(InformatieCont.username, "");
            else
                mesaj = InformatieCont.username2;
            mesajLogat.setText(mesaj);
            InformatieCont.fakeCall2=InformatieCont.sharedPreferences.getString(InformatieCont.fakeCallOn,"");
        }
        progres = new ProgressDialog(this);
        sosBtn = findViewById(R.id.sosBtn);
        watchBtn = findViewById(R.id.watchBtn);
        locationBtn = findViewById(R.id.locationBtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        stealBtn = findViewById(R.id.stealBtn);
        fakeCallBtn = findViewById(R.id.fakeCallBtn);


        sosBtn.setOnClickListener(v -> {
            Intent intentChange = new Intent(this, SosMenu.class);
            startActivity(intentChange);
            overridePendingTransition(R.anim.slide_down_foreground, R.anim.slide_down_background);
        });

        watchBtn.setOnClickListener(v -> {
            Intent intentChange = new Intent(this, SafetyTimeActivity.class);
            startActivity(intentChange);
            overridePendingTransition(R.anim.slide_down_foreground, R.anim.slide_down_background);
        });

        locationBtn.setOnClickListener(v-> {
            Intent intentLocation=new Intent(this,LocationMenu.class);
            startActivity(intentLocation);
            overridePendingTransition(R.anim.slide_down_foreground, R.anim.slide_down_background);

        });
        fakeCallBtn.setOnClickListener(v-> {
            Intent intent4=new Intent(this, InitializationCall.class);
            startActivity(intent4);
            overridePendingTransition(R.anim.slide_down_foreground, R.anim.slide_down_background);
        });


        pathRecord=getRecordingFilePath();
        if(isMicrophonePresent())
        {
            getMicrofonPermision();
        }

        cameraBtn.setOnClickListener(v-> {
                    Intent intentRecorder = new Intent(this, RecorderMenu.class);
                    startActivity(intentRecorder);
                });

        stealBtn.setOnClickListener(v -> {
            Intent intentSteal = new Intent(this, StealActivity.class);
            startActivity(intentSteal);
            overridePendingTransition(R.anim.slide_down_foreground, R.anim.slide_down_background);
        });

        StealActivity.generateList();

        InformatieCont.sharedPreferences = getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        if (InformatieCont.sharedPreferences.contains(InformatieCont.steal) && InformatieCont.sharedPreferences.getString(InformatieCont.steal, "").equals("on")) {
            Intent cameraService = new Intent(this, CameraService.class);
            if (!isMyServiceRunning(CameraService.class)) {
                startService(cameraService);
            }
        }

    }

    private static String getRecordingFilePath()
    {
        ContextWrapper contextWrapper=new ContextWrapper(context.getApplicationContext());
        File musicD=contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file=new File(musicD,"recording_" + Calendar.getInstance().getTime() +".mp3");
        return file.getPath();
    }

    private  boolean isMicrophonePresent()
    {
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
        {
            return true;
        }
        return false;
    }
    private void getMicrofonPermision(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},200);
        }
    }

    private void requestPermissions() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_DENIED) {

            Log.d("permission", "permission denied to SEND_SMS - requesting it");
            String[] permissions = {Manifest.permission.SEND_SMS};

            requestPermissions(permissions, PERMISSION_REQUEST_CODE);

        }
    }

    /**
     * Momentan am legat aceasta functie de butonul de settings pana cand se
     * va realiza butonul de logout
     * Odata cu delogarea datele care sunt salvate in fisiere sharedPreferences sunt sterse
     * @param view
     */
    public void deconect(View view)
    {
        settings=findViewById(R.id.settings2);

        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
        InformatieCont.username2 = null;
        editor.clear();
        editor.apply();

        stopService(new Intent(this, ScreenOnOffBackgroundService.class));

        Intent sessionIntent = new Intent(this, LandingActivity.class);
        sessionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(sessionIntent);
        finish();
    }

    /**
     * Opreste fortat iesirea pe butonul back
     */
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(false);
//    }

    /**
     * METHOD NOT APPROVED, TO BE DISCUSSED
     * The method is showing a button on the dashboard.xml that can force close the application
     * Useful if you want to close the background service in a user friendly manner
     */
    private void showForceCloseButton() {
//        Button forceClose = findViewById(R.id.forceCloseButton);
//        forceClose.setVisibility(View.VISIBLE);
//        forceClose.setOnClickListener(v -> {
//            finish();
//            System.exit(0);
//        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // if the activity was opened from the notification background service
        // but the activity was already running, we are going to show the force close button
        if (intent != null && intent.getAction().equals("showForceClose")) {
            showForceCloseButton();
            Toast.makeText(this,
                    "apasat din notificare, aplicatia era deschisa",
                    Toast.LENGTH_LONG).show();
        }
    }
    /**
     * The method is looping in every running services to see if the parameter of the function is
     * already running
     * The general purpose of method is to not have a service running in multiple instances
     *
     * @param  serviceClass  generic class of the activity
     * @return      true if the inspected service is online, otherwise false
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceIterator : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceIterator.service.getClassName() != null && serviceClass.getName() != null && serviceClass.getName().equals(serviceIterator.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}