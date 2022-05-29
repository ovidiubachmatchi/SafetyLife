package com.protect.safetylife.controller.safetytime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.utils.SMSService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
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
    private static NotificationChannel channel;

    private final BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            millisUntilFinished = intent.getLongExtra(TimerService.TIME_REMAINING, 0);
            textViewTime.setText(timeFormatter(millisUntilFinished));
            progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            if(millisUntilFinished == 0) {
                stopCountDown();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("contacts").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && document.getData().get("smsContacts") != null) {
                            for (String numar :(ArrayList<String>) Objects.requireNonNull(document.getData().get("smsContacts"))) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(numar, null, "Safety Time! Probabil am nevoie de ajutor.", null, null);
                            }
                        }
                    } else {
                        System.out.println("crapa");
                        Log.d("getContact", "get failed with ", task.getException());
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(channel == null) {
                channel = new NotificationChannel("safetyTime", "SafetyTime", NotificationManager.IMPORTANCE_LOW);
                channel.setLightColor(Color.BLUE);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                getSystemService((NotificationManager.class)).createNotificationChannel(channel);
            }
        }

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
    static  String timeFormatter(long milliSeconds) {
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
        if (counterIsActive) {
            stopCountDown();
            Toast.makeText(this, "Countdown stopped: " + timeFormatter(millisUntilFinished), Toast.LENGTH_LONG).show();
        } else {
            if(getCountDownDuration() <  30000){
                Toast.makeText(this, "Minimum time is 30 seconds", Toast.LENGTH_LONG).show();
                return;
            }
            startCountDown();
            Toast.makeText(this, "Countdown started: " + timeFormatter(timeTotalInMilliSeconds), Toast.LENGTH_LONG).show();
        }
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
        numberPickerHours.setEnabled(true);
        numberPickerMinutes.setEnabled(true);
        numberPickerSeconds.setEnabled(true);
    }

    static void pushNotification(Context context, String title, String content){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                new Intent(context, SafetyTimeActivity.class).setAction("intentForceClose"), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, "safetyTime")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(Color.GRAY)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .build();

        int id = new Random(System.currentTimeMillis()).nextInt(1000);
        NotificationManagerCompat.from(context).notify(id, notification);
    }
}