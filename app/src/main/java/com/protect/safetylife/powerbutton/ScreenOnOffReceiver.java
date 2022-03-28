package com.protect.safetylife.powerbutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

public class ScreenOnOffReceiver extends BroadcastReceiver {
    private final Context context;
    private int counter = 0;
    public ScreenOnOffReceiver(Context context) {
        this.context = context;
    }

    private final CountDownTimer timer = new CountDownTimer(1500, 1000) {
        @Override
        public void onTick(long l) { }

        @Override
        public void onFinish() {
            counter = 0;
        }
    };

    public void count() {
        counter++;
        if(counter == 5) {
            Toast.makeText(context, "Power Button pressed 5 times", Toast.LENGTH_LONG).show();
            return;
        }
        timer.cancel();
        timer.start();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
            count();
        }

    }
}