package com.protect.safetylife.controller.powerbutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.utils.SMSService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

    private void count() {
        counter++;
        if(counter == 5) {
            serviceStart();
            return;
        }
        timer.cancel();
        timer.start();
    }

    private void serviceStart() {
        Toast.makeText(context, "Power Button pressed 5 times", Toast.LENGTH_LONG).show();
        // TODO HARDCODED PHONE NUMBERS
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("contacts").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists() && document.getData().get("smsContacts") != null) {
                    SMSService.sendSMS((ArrayList) Objects.requireNonNull(document.getData().get("smsContacts")));
                }
            } else {
                Log.d("getContact", "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
            count();
        }
    }
}