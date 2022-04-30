package com.protect.safetylife.controller.safetytime;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import java.util.Objects;

public class TimerService extends Service {
    static final String COUNTDOWN_UPDATED = "COUNTDOWN_UPDATED";
    static final String TIME_REMAINING = "TIME_REMAINING";

    private CountDownTimer countDownTimer;
    private Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        countDownTimer = new CountDownTimer(intent.getLongExtra(TIME_REMAINING, 0), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendBroadcast(new Intent(COUNTDOWN_UPDATED).putExtra(TIME_REMAINING, millisUntilFinished));
                if ((float) millisUntilFinished / 1000 < 5 * 60 && (float) millisUntilFinished / 1000 > 5 * 60 - 1)
                    SafetyTimeActivity.pushNotification(context, "Countdown active", "5 minutes left");
                else if ((float) millisUntilFinished / 1000 < 30 * 60 && (float) millisUntilFinished / 1000 > 30 * 60 - 1)
                    SafetyTimeActivity.pushNotification(context, "Countdown active", "30 minutes left");
                else if ((float) millisUntilFinished / 1000 < 60 * 60 && (float) millisUntilFinished / 1000 > 60 * 60 - 1)
                    SafetyTimeActivity.pushNotification(context, "Countdown active", "1 hour left");
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