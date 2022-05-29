package com.protect.safetylife.controller.powerbutton;

import static com.protect.safetylife.controller.dashboard.SosMenu.historyList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.protect.safetylife.utils.SMSService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScreenOnOffReceiver extends BroadcastReceiver {
    private final Context context;
    private int counter = 0;
    private FirebaseFirestore dbSOSMENU;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void count() {
        counter++;
        if(counter == 5) {
            serviceStart();
            return;
        }
        timer.cancel();
        timer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void serviceStart() {
        addSOSCallToDatabase();
        Toast.makeText(context, "Power Button pressed 5 times", Toast.LENGTH_LONG).show();
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void addSOSCallToDatabase() {
            dbSOSMENU = FirebaseFirestore.getInstance();
            Map<String, Object> time = new HashMap<>();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            ArrayList<String> historyDB = new ArrayList<>();

            DocumentReference docRef = dbSOSMENU.collection("soshistory").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && (document.getData().get("time") != "" || document.getData().get("time") != null)) {
                        for (Object smsContact : (ArrayList) Objects.requireNonNull(document.getData().get("time"))) {
                            historyDB.add((String) smsContact);
                        }

                    }
                    historyDB.add(dtf.format(now));
                    time.put("time", historyDB);

                    dbSOSMENU.collection("soshistory").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .set(time, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> Log.d("smsContacts DB", "Successfully written!"))
                            .addOnFailureListener(e -> Log.w("smsContacts DB", "Error writing document", e));

                } else {
                    Log.d("getContact", "get failed with ", task.getException());
                }
            });

       }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
            count();
        }
    }
}