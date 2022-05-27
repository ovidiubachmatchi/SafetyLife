package com.protect.safetylife.location;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.controller.dashboard.LocationMenu;
import com.protect.safetylife.controller.signup.SignUp4Activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Location {


    public Location()
    {
        Location.locationDetect(LocationMenu.context);
    }
    @SuppressLint("MissingPermission")
    public static void locationDetect(Context context)
    {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        System.out.println("inainte");
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    android.location.Location location=task.getResult();
                    System.out.println(location);
                    if(location!=null)
                    {
                        try {
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            System.out.println(addresses.get(0).getLatitude());
                            //s.add(addresses.get(0).getLatitude()+""); // latitudinea
                            //s.add(addresses.get(0).getLongitude()+""); //longitudinea
                            //s.add(addresses.get(0).getCountryName()+"");//tara
                            //s.add(addresses.get(0).getLocality()+""); //localitatea
                           // s.add(addresses.get(0).getAddressLine(0)+""); //adresa totala
                            FirebaseAuth auth=FirebaseAuth.getInstance();
                            FirebaseUser user=auth.getCurrentUser();
                            FirebaseFirestore db=FirebaseFirestore.getInstance();// initializam instanta la baza de date
                            if(user!=null) {
                                addBaseLocation(user,db,addresses);
                            }
                            else
                                Toast.makeText(context, addresses.get(0).getLatitude()+", "+addresses.get(0).getLongitude()+", "+addresses.get(0).getCountryName()+", "+addresses.get(0).getLocality()+", "+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addBaseLocation(FirebaseUser user, FirebaseFirestore db, List<Address> addresses)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
        Map<String,Object> info=new HashMap<>();
        info.put("Latitude",addresses.get(0).getLatitude());
        info.put("Longitude",addresses.get(0).getLongitude());
        info.put("Country",addresses.get(0).getCountryName());
        info.put("Locality",addresses.get(0).getLocality());
        info.put("Address",addresses.get(0).getAddressLine(0));
        db.collection("location_history").document(user.getUid()).collection(dtf.format(now)+"").document(dtf2.format(now)+"").set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(DashboardActivity.context, "Location saves on database!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashboardActivity.context, "Error saves location try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("MissingPermission")
    public static ArrayList<String> locationSend(Context context)
    {
        ArrayList<String> s=new ArrayList<>();

        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        System.out.println("Trimis mesaj");
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                android.location.Location location=task.getResult();
                System.out.println(location);
                if(location!=null)
                {
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        System.out.println(addresses.get(0).getLatitude());
                        s.add(addresses.get(0).getLatitude()+""); // latitudinea
                        s.add(addresses.get(0).getLongitude()+""); //longitudinea
                        s.add(addresses.get(0).getCountryName()+"");//tara
                        s.add(addresses.get(0).getLocality()+""); //localitatea
                        s.add(addresses.get(0).getAddressLine(0)+""); //adresa totala
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return s;
    }
}
