package com.protect.safetylife.controller.powerbutton;

import static android.content.Context.VIBRATOR_SERVICE;



import static androidx.core.content.ContextCompat.getSystemService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.recorder.Recorder;
import com.protect.safetylife.utils.SMSService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import com.protect.safetylife.Informatii.InformatieCont;
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
          //  serviceStart();
            if(InformatieCont.fakeCall2==null || InformatieCont.fakeCall2.equals("on"))
                fakeCall();
            serviceStart();

            recorder();
            //recorder();
            return;
        }
        timer.cancel();
        timer.start();
    }

    private void recorder()
    {
        CountDownTimer countDowntimer = new CountDownTimer(30000, 30000) {
            public void onTick(long millisUntilFinished) {
                Toast.makeText(context, "A pornit", Toast.LENGTH_LONG).show();
                Recorder.startRecording2();
            }


            public void onFinish() {
                Toast.makeText(context, "S-a oprit", Toast.LENGTH_LONG).show();
                Recorder.stopRecording();

            }};countDowntimer.start();
    }

    private void fakeCall()
    {
        Toast.makeText(context, "Power Button pressed 5 times fakecall", Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Vibrator vibrator=(Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
            Intent intent3 = new Intent(Intent.ACTION_CALL);
            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(InformatieCont.phoneFake2!=null)
            intent3.setData(Uri.parse("tel: "+ InformatieCont.phoneFake2));
            else
                intent3.setData(Uri.parse("tel: 0722222222"));
            context.startActivity(intent3);
        }
    }


    private void serviceStart() {
        Toast.makeText(context, "Power Button pressed 5 times sms", Toast.LENGTH_LONG).show();
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