package com.protect.safetylife.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<<<< Temporary merge branch 1:app/src/main/java/com/protect/safetylife/controller/LandingActivity.java
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.controller.login.LogInActivity;
import com.protect.safetylife.controller.signup.SignUp1Activity;
=========
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.dashboard.DashboardActivity;
import com.protect.safetylife.login.LogInActivity;
import com.protect.safetylife.signup.SignUp1Activity;
>>>>>>>>> Temporary merge branch 2:app/src/main/java/com/protect/safetylife/LandingActivity.java

public class LandingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        /**
         * Initializam si verificam daca este logat
         */
        InformatieCont.sharedPreferences= getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        if(InformatieCont.verificareLogat())  // verificare sesiune logare
        {
            Intent activity2=new Intent(this,DashboardActivity.class);
            startActivity(activity2);
        }
        addButtonsFunctionality();
    }


    private void addButtonsFunctionality() {
        ImageView loginBtn = findViewById(R.id.button_white_fill);
        ImageView signupBtn = findViewById(R.id.button_empty);
        TextView skipLoginBtn = findViewById(R.id.skipLoginBtn);

        skipLoginBtn.setOnClickListener(v -> {
            skipLoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {
            loginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        });

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            Intent intent = new Intent(this, SignUp1Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        });

    }


}
