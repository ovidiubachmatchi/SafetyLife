package com.protect.safetylife.controller.safetytime;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import java.util.Objects;

public class TimerService extends Service {
    private static CountDownTimer countDownTimer;
    private static long timeCountInMilliSeconds;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timeCountInMilliSeconds = intent.getLongExtra("TIME_COUNT", 0);
        System.out.println(timeCountInMilliSeconds+"\n\n\n\n\n");

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Intent intent = new Intent("TIMER_UPDATED");
                intent.putExtra("TIME_REMAINING", millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!Objects.isNull(countDownTimer))
            countDownTimer.cancel();
    }
}