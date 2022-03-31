package com.protect.safetylife.controller.dashboard;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.controller.powerbutton.ScreenOnOffBackgroundService;

/**
    Main activity
*/
public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting visual layout
        setContentView(R.layout.menu);
        // starting background service
        Intent backgroundService = new Intent(this, ScreenOnOffBackgroundService.class);
        // making sure the service is not running in multiple instances
        if (!isMyServiceRunning(ScreenOnOffBackgroundService.class)) {
            startService(backgroundService);
        }
        // if the activity was opened from the notification background service
        // we are going to show the force close button
        Intent intent = getIntent();
        if (intent != null && "intentForceClose".equals(intent.getAction())) {
            showForceCloseButton();
            Toast.makeText(this,
                    "apasat din notificare, aplicatia era inchisa",
                    Toast.LENGTH_LONG).show();
        }

        ImageView sosBtn = findViewById(R.id.sosBtn);
        ImageView watchBtn = findViewById(R.id.watchBtn);
        ImageView locationBtn = findViewById(R.id.locationBtn);
        ImageView cameraBtn = findViewById(R.id.cameraBtn);
        ImageView stealBtn = findViewById(R.id.stealBtn);
        ImageView reminderBtn = findViewById(R.id.reminderBtn);
        ImageView fakeCallBtn = findViewById(R.id.fakeCallBtn);


        sosBtn.setOnClickListener(v -> {
            Intent intentChange = new Intent(this, SosMenu.class);
            startActivity(intentChange);
            overridePendingTransition(R.anim.slide_down_foreground, R.anim.slide_down_background);
        });

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
        // activity destroyed
        // TODO
        // If user is not logged, destroy ScreenOnOffServiee
    }
}