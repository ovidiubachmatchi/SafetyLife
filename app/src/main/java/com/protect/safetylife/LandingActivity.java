package com.protect.safetylife;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.login.LogInActivity;
import com.protect.safetylife.signup.SignUpActivity;

public class LandingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        addButtonsFunctionality();
    }


    private void addButtonsFunctionality() {
        ImageView loginBtn = findViewById(R.id.button_white_fill);
        ImageView signupBtn = findViewById(R.id.button_empty);
        TextView skipLoginBtn = findViewById(R.id.skipLoginBtn);

        skipLoginBtn.setOnClickListener(v -> {
            skipLoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
        });

        loginBtn.setOnClickListener(v -> {
            loginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        });

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        });

    }


}
