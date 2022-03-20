package com.protect.safetylife.signup;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.Utils.Utils;

public class SignUp4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup4);
        addButtonFunctionality();
        Utils.fadeInAfterDuration(findViewById(R.id.back), 450);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
        Utils.fadeOut(findViewById(R.id.back), 10);
    }

    private void addButtonFunctionality() {
        ImageView backBtn = findViewById(R.id.back);
        ImageView signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));

        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            finish();
        });

    }


}
