package com.protect.safetylife;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


public class ScreenOnOffBackgroundService extends Service {

    private ScreenOnOffReceiver screenOnOffReceiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onStartCommand", "Service onStartCommand: screenOnOffReceiver is started.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        //  we are starting a foreground service differently for
        //  build versions greater than Android Oreo
        //  for compatibility
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            // NOTIFICARE CUSTOM
            startMyOwnForeground();
        else
            // NOTIFICATE DEFAULT
            startForeground(1, new Notification());
        
        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");

        // Set broadcast receiver priority.
        intentFilter.setPriority(100);

        // Create a network change broadcast receiver.
        screenOnOffReceiver = new ScreenOnOffReceiver(this);

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(screenOnOffReceiver, intentFilter);

        Log.d("SCREEN_TOGGLE_TAG", "Service onCreate: screenOnOffReceiver is registered.");
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)

                // fara .setSmallIcon, contentul notificarii nu se schimba
                .setSmallIcon(R.drawable.background_notification_icon)
                .setContentTitle("SafetyLife is running in background")
                .setColor(Color.GRAY)
                .setContentText(" descriere notificare ") // to be modified
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister screenOnOffReceiver when destroy.
        if(screenOnOffReceiver!=null)
        {
            unregisterReceiver(screenOnOffReceiver);
            Log.d("SCREEN_TOGGLE_TAG", "Service onDestroy: screenOnOffReceiver is unregistered.");
        }
    }
}