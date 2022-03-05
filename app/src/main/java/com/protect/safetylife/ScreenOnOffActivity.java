package com.protect.safetylife;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
    Main activity
*/
public class ScreenOnOffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(),"ScreenOnOffActivity created",Toast.LENGTH_SHORT).show();

        // setting visual layout
        setContentView(R.layout.auth_landing);
        // starting app service in background
        Intent backgroundService = new Intent(this, ScreenOnOffBackgroundService.class);
        // making sure the service is not running in multiple instances
        if (!isMyServiceRunning(ScreenOnOffBackgroundService.class)) {
            Toast.makeText(getApplicationContext(),"ScreenOnOffBackgroundService created",Toast.LENGTH_SHORT).show();
            startService(backgroundService);
        }
        else {
            Toast.makeText(getApplicationContext(),"ScreenOnOffBackgroundService already running in background",Toast.LENGTH_SHORT).show();
        }
        // info activity started

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
            if (serviceClass.getName().equals(serviceIterator.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // info activity destroyed
        Toast.makeText(getApplicationContext(),"ScreenOnOffActivity destroyed",Toast.LENGTH_SHORT).show();
    }
}