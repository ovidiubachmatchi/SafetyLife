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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SafetyTimeActivity extends AppCompatActivity {
    private TextView textViewTime;
    private Button buttonStartStop;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBarCircle;
    private static Intent serviceIntent;
    private long millisUntilFinished;
    private long timeCountInMilliSeconds = 200L * 1000;
    private static Boolean counterIsActive = false;

    private NumberPicker numberPickerSeconds;
    private NumberPicker numberPickerMinutes;
    private NumberPicker numberPickerHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_time);

        textViewTime = findViewById(R.id.textViewTime);
        buttonStartStop = findViewById(R.id.buttonStartStop);
        progressBarCircle = findViewById(R.id.progressBarCircle);

        numberPickerSeconds = findViewById(R.id.numberPickerSeconds);
        numberPickerSeconds.setMaxValue(60);
        numberPickerSeconds.setMinValue(0);

        numberPickerMinutes = findViewById(R.id.numberPickerMinutes);
        numberPickerMinutes.setMaxValue(60);
        numberPickerMinutes.setMinValue(0);

        numberPickerHours = findViewById(R.id.numberPickerHours);
        numberPickerHours.setMaxValue(24);
        numberPickerHours.setMinValue(0);

        if(counterIsActive){
            registerReceiver(updateTime, new IntentFilter("TIMER_UPDATED"));
            buttonStartStop.setText("STOP");
        }
    }

    private BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            millisUntilFinished = intent.getLongExtra("TIME_REMAINING", 0);
            textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
            progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
        }
    };

    @SuppressLint("SetTextI18n")
    public void startStop(View view) {
        if (!counterIsActive) {
            counterIsActive = true;
            buttonStartStop.setText("STOP");
            setProgressBarValues();

            if(Objects.isNull(serviceIntent)) {
                serviceIntent = new Intent(this, TimerService.class);
                registerReceiver(updateTime, new IntentFilter("TIMER_UPDATED"));

                serviceIntent.putExtra("TIME_COUNT", timeCountInMilliSeconds);
                startService(serviceIntent);
            }
        } else {
            reset();
        }
    }

    private String hmsTimeFormatter(long milliSeconds) {
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return time;
    }

    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    @SuppressLint("SetTextI18n")
    private void reset() {
        countDownTimer.cancel();
        counterIsActive = false;
        buttonStartStop.setText("START");
        setProgressBarValues();
    }
}