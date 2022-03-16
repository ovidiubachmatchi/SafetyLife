package com.protect.safetylife.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addButtonsAnimation();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
    }

    private void addButtonsAnimation() {
        ImageView backBtn = findViewById(R.id.back);
        ImageView loginBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(v -> {
            loginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            checkCredentials();
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            finish();
        });

    }

    private void checkCredentials() {
        EditText EmailAddress = findViewById(R.id.editTextTextEmailAddress);
        EditText Password = findViewById(R.id.editTextTextPassword);
        if(TextUtils.isEmpty(EmailAddress.getText().toString())
                &&
                TextUtils.isEmpty(Password.getText().toString())) {
            EmailAddress.setHint("Enter your email address");
            Password.setHint("Enter your password");
            return;
        }

        if(TextUtils.isEmpty(EmailAddress.getText().toString())) {
            EmailAddress.setHint("Enter your email address");
            return;
        }
        if(TextUtils.isEmpty(Password.getText().toString())) {
            Password.setHint("Enter your password");
            return;
        }
    }

}
