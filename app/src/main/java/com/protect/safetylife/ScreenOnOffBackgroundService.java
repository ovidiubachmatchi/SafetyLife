package com.protect.safetylife;

import static android.content.Intent.FLAG_ACTIVITY_TASK_ON_HOME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "safetylife";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        // launch activity when clicking on background service notification
        Intent resultIntent = new Intent(this, ScreenOnOffActivity.class);
        resultIntent.setAction("showForceClose");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.background_notification_icon)
                .setColor(Color.GRAY)
                .setContentTitle("SafetyLife is running in background")
                .setContentText("Click to stop the application from listening to you in moments of panic") // to be modified
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(resultPendingIntent)
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