package com.protect.safetylife.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.animations.Animation;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addButtonFunctionality();
        Animation.fadeInAfterDuration(findViewById(R.id.back), 600);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
        Animation.fadeOut(findViewById(R.id.back), 0);
    }

    private void addButtonFunctionality() {
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
    /** to be continued */
    private void checkCredentials() {
        EditText EmailAddress = findViewById(R.id.firstName);
        EditText Password = findViewById(R.id.dateOfBirth);
        if(TextUtils.isEmpty(EmailAddress.getText().toString())
                &&
           TextUtils.isEmpty(Password.getText().toString())) {
            //
        }
    }

}
