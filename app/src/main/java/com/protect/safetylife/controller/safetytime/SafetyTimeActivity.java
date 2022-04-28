package com.protect.safetylife.controller.safetytime;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.protect.safetylife.R;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SafetyTimeActivity extends AppCompatActivity {
    private TextView textViewTime;
    private Button buttonStartStop;
    private ProgressBar progressBarCircle;
    private NumberPicker numberPickerSeconds;
    private NumberPicker numberPickerMinutes;
    private NumberPicker numberPickerHours;

    private static long millisUntilFinished;
    private static long timeTotalInMilliSeconds;
    private static Boolean counterIsActive = false;
    private static Intent serviceIntent;

    private final BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            millisUntilFinished = intent.getLongExtra(TimerService.TIME_REMAINING, 0);
            textViewTime.setText(timeFormatter(millisUntilFinished));
            progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            if(millisUntilFinished == 0)
                stopCountDown();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_time);
        viewsSetup();
    }

    private void viewsSetup() {
        textViewTime = findViewById(R.id.textViewTime);
        buttonStartStop = findViewById(R.id.buttonStartStop);
        progressBarCircle = findViewById(R.id.progressBarCircle);
        numberPickerSeconds = findViewById(R.id.numberPickerSeconds);
        numberPickerMinutes = findViewById(R.id.numberPickerMinutes);
        numberPickerHours = findViewById(R.id.numberPickerHours);

        numberPickerSeconds.setMaxValue(59);
        numberPickerSeconds.setMinValue(0);

        numberPickerMinutes.setMaxValue(59);
        numberPickerMinutes.setMinValue(0);

        numberPickerHours.setMaxValue(23);
        numberPickerHours.setMinValue(0);

        if (counterIsActive) {
            counterActiveSetup();
        }
    }

    @SuppressLint("SetTextI18n")
    private void counterActiveSetup(){
        registerReceiver(updateTime, new IntentFilter(TimerService.COUNTDOWN_UPDATED));
        textViewTime.setText(timeFormatter(millisUntilFinished));
        progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
        buttonStartStop.setText("STOP");

        numberPickerHours.setEnabled(false);
        numberPickerMinutes.setEnabled(false);
        numberPickerSeconds.setEnabled(false);

        progressBarCircle.setMax((int) timeTotalInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeTotalInMilliSeconds / 1000);
    }

    @SuppressLint("DefaultLocale")
    private String timeFormatter(long milliSeconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }

    private long getCountDownDuration() {
        int hours = numberPickerHours.getValue();
        int minutes = numberPickerMinutes.getValue();
        int seconds = numberPickerSeconds.getValue();

        return (seconds + 60L * minutes + 3600L * hours) * 1000L;
    }

    public void startStop(View view) {
        if (counterIsActive)
            stopCountDown();
        else
            startCountDown();
    }

    private void startCountDown(){
        serviceIntent = new Intent(this, TimerService.class);
        timeTotalInMilliSeconds = getCountDownDuration();
        counterIsActive = true;

        counterActiveSetup();

        serviceIntent.putExtra(TimerService.TIME_REMAINING, timeTotalInMilliSeconds);
        startService(serviceIntent);
    }

    @SuppressLint("SetTextI18n")
    private void stopCountDown(){
        stopService(serviceIntent);
        counterIsActive = false;
        buttonStartStop.setText("START");
        progressBarCircle.setProgress((int) (timeTotalInMilliSeconds / 1000));
        numberPickerHours.setEnabled(true);
        numberPickerMinutes.setEnabled(true);
        numberPickerSeconds.setEnabled(true);
    }
}