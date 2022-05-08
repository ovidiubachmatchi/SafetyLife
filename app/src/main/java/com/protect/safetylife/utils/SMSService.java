package com.protect.safetylife.utils;

import static com.protect.safetylife.Informatii.InformatieCont.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.controller.dashboard.DashboardActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SMSService {
    private static String messageToSend = "-";

    @SuppressLint("MissingPermission")
    public static void sendSMS(List<String> numereSMS) {
        if (DashboardActivity.context == null)
            return;

        Thread x = new Thread() {
            @Override
            public void run() {
                System.out.println(1);
                try {
                    while(true){
                        System.out.println("wait");
                        if(messageToSend != "-")
                            break;
                    }
                    System.out.println("complete");
                    for (String numar : numereSMS) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(numar, null, messageToSend, null, null);
                    }
                    System.out.println("sent");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        x.start();

        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DashboardActivity.context);
        System.out.println("Trimis mesaj");
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                android.location.Location location=task.getResult();
                System.out.println(location);
                if(location!=null)
                {
                    try {

                        Geocoder geocoder = new Geocoder(DashboardActivity.context, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        System.out.println(addresses.get(0).getLatitude());
                        messageToSend +=
                                "SAFETY ALERT - "
                                        +
                                        addresses.get(0).getLatitude() +
                                        addresses.get(0).getLongitude() +
                                        addresses.get(0).getCountryName() +
                                        addresses.get(0).getLocality() +
                                        addresses.get(0).getAddressLine(0);

                        Intent intent1 = new Intent("locatie");
                        intent1.putExtra("locatie",messageToSend);
                        DashboardActivity.context.sendBroadcast(intent1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String locatie = intent.getStringExtra("locatie");

            SMSService.messageToSend = locatie;
            System.out.println(locatie);

        }
    };
}