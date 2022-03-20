package com.protect.safetylife.signup;

import static com.protect.safetylife.Utils.Utils.errorInputBox;
import static com.protect.safetylife.Utils.Utils.validInputBox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.Utils.Utils;

public class SignUp3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup3);
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
            if (validCredentials()) {
                Intent intent = new Intent(this, SignUp4Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            finish();
        });

    }

    private boolean validCredentials() {
        EditText street = findViewById(R.id.street);
        EditText country = findViewById(R.id.country);
        EditText zipcode = findViewById(R.id.zipcode);

        boolean valid = true;

        if(TextUtils.isEmpty(street.getText().toString())) {
            errorInputBox(street, this);
            valid = false;
        }
        else {
            validInputBox(street, this);
        }

        if(TextUtils.isEmpty(country.getText().toString())) {
            errorInputBox(country, this);
            valid = false;
        }
        else {
            validInputBox(country, this);
        }

        if(TextUtils.isEmpty(zipcode.getText().toString())) {
            errorInputBox(zipcode, this);
            valid = false;
        }
        else {
            validInputBox(zipcode, this);
        }

        return valid;
    }

}
