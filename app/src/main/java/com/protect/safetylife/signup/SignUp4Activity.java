package com.protect.safetylife.signup;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;

public class SignUp4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup4);
        addButtonFunctionality();
        Animation.fadeInAfterDuration(findViewById(R.id.back), 450);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
        Animation.fadeOut(findViewById(R.id.back), 10);
    }

    private void addButtonFunctionality() {
        ImageView backBtn = findViewById(R.id.back);
        ImageView signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));

        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });

    }


}
