package com.protect.safetylife.controller.safetytime;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import java.util.Objects;

public class TimerService extends Service {
    static final String COUNTDOWN_UPDATED = "COUNTDOWN_UPDATED";
    static final String TIME_REMAINING = "TIME_REMAINING";

    private CountDownTimer countDownTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        countDownTimer = new CountDownTimer(intent.getLongExtra(TIME_REMAINING, 0), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendBroadcast(new Intent(COUNTDOWN_UPDATED).putExtra(TIME_REMAINING, millisUntilFinished));
            }

            @Override
            public void onFinish() {
                sendBroadcast(new Intent(COUNTDOWN_UPDATED).putExtra(TIME_REMAINING, 0));
                countDownTimer.cancel();
            }
        };

        countDownTimer.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if(!Objects.isNull(countDownTimer))
            countDownTimer.cancel();

        super.onDestroy();
    }
}