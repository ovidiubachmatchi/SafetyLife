package com.protect.safetylife.controller.dashboard;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.powerbutton.ScreenOnOffBackgroundService;
import com.protect.safetylife.controller.safetytime.SafetyTimeActivity;

/**
    Main activity
*/
public class DashboardActivity extends AppCompatActivity {

    private TextView mesajLogat;
    private ProgressDialog progres;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ImageView sosBtn;
    private ImageView watchBtn;
    private ImageView locationBtn;
    private ImageView cameraBtn;
    private ImageView stealBtn;
    private ImageView reminderBtn;
    private ImageView fakeCallBtn;
    private ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting visual layout
        setContentView(R.layout.menu);
        // starting background service
        Intent backgroundService = new Intent(this, ScreenOnOffBackgroundService.class);


        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
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
        InformatieCont.sharedPreferences= getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        mesajLogat = findViewById(R.id.mesajLogat);
        if(InformatieCont.verificareLogat())
        {
            String mesaj = "";
            if(InformatieCont.username2 == null)
                mesaj = InformatieCont.sharedPreferences.getString(InformatieCont.username,"");
            else
                mesaj = InformatieCont.username2;
            mesajLogat.setText(mesaj);
        }
        progres=new ProgressDialog(this);
        sosBtn = findViewById(R.id.sosBtn);
        watchBtn = findViewById(R.id.watchBtn);
        locationBtn = findViewById(R.id.locationBtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        stealBtn = findViewById(R.id.stealBtn);
        reminderBtn = findViewById(R.id.reminderBtn);
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
        SharedPreferences.Editor editor=InformatieCont.sharedPreferences.edit();
        editor.clear();
        editor.commit();
        stopService(new Intent(this, ScreenOnOffBackgroundService.class));
        finish();
    }

    /**
     * Opreste fortat iesirea pe butonul back
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

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