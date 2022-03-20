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
import androidx.appcompat.content.res.AppCompatResources;

import com.protect.safetylife.R;
import com.protect.safetylife.Utils.Utils;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        addButtonFunctionality();
        Utils.fadeInAfterDuration(findViewById(R.id.back), 450);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
        Utils.fadeOut(findViewById(R.id.back), 100);
    }

    private void addButtonFunctionality() {
        ImageView backBtn = findViewById(R.id.back);
        ImageView signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            if (validCredentials()) {
                Intent intent = new Intent(this, SignUp2Activity.class);
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
        EditText emailAddress = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText repeatPassword = findViewById(R.id.repeatPassword);

        boolean valid = true;

        if(TextUtils.isEmpty(emailAddress.getText().toString())) {
            errorInputBox(emailAddress, this);
            valid = false;
        }
        else {
            validInputBox(emailAddress, this);
        }

        if(TextUtils.isEmpty(password.getText().toString())) {
            errorInputBox(password, this);
            valid = false;
        }
        else {
            validInputBox(password, this);
        }

        if(TextUtils.isEmpty(repeatPassword.getText().toString())) {
            errorInputBox(repeatPassword, this);
            valid = false;
        }
        else {
            validInputBox(repeatPassword, this);
        }

        return valid;
    }


}
