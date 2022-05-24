package com.protect.safetylife.location;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.protect.safetylife.R;

public class MapsLocation extends FragmentActivity implements OnMapReadyCallback {

    private String longitudine,latitudine,location;
    private String[] elem;
    private TextView ed1,ed2,ed3,ed4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_location);
        location=getIntent().getStringExtra("locatie");
        elem=location.split(",");
        latitudine=elem[4];
        longitudine=elem[5];
        ed1=findViewById(R.id.timeMap2);
        ed2=findViewById(R.id.latitudeMap2);
        ed3=findViewById(R.id.longitudeMap2);
        ed4=findViewById(R.id.addressMap2);
        ed1.setText(elem[0].split(":")[1]+":"+elem[0].split(":")[2]);
        ed2.setText(elem[4]);
        ed3.setText(elem[5]);
        ed4.setText(elem[1]+", "+elem[2]+", "+elem[3]);
        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(MapsLocation.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        double lat=Double.parseDouble(latitudine);
        double lon=Double.parseDouble(longitudine);
        LatLng latLng=new LatLng(lat,lon);
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title(elem[1]+", "+elem[2]+", "+elem[3]);

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
        googleMap.addMarker(markerOptions);
    }
}