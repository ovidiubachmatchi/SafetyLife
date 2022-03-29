package com.protect.safetylife.login;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;
import com.protect.safetylife.utils.Credentials;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addButtonFunctionality();
        Animation.fadeIn(findViewById(R.id.back), 600);
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
            loginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            if (validCredentials()) {
                //
            }
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });

    }

    private boolean validCredentials() {
        EditText emailAddress = findViewById(R.id.emailLogin);
        EditText password = findViewById(R.id.passwordLogin);

        boolean valid = Credentials.isEmpty(this, emailAddress, password);

        return valid;
    }

}
