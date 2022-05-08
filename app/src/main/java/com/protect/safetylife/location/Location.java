package com.protect.safetylife.location;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.protect.safetylife.controller.dashboard.LocationMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                            Toast.makeText(context, addresses.get(0).getLatitude()+", "+addresses.get(0).getLongitude()+", "+addresses.get(0).getCountryName()+", "+addresses.get(0).getLocality()+", "+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
}
