package com.protect.safetylife.controller.signup;

import static android.content.ContentValues.TAG;

import static com.protect.safetylife.utils.Animation.errorInputBox;
import static com.protect.safetylife.utils.Animation.validInputBox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.firebase.auth.FirebaseAuth;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.utils.Credentials;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;

import java.util.Objects;

public class SignUp1Activity extends AppCompatActivity {
    private EditText emailAddress;
    private  EditText password;
    private EditText repeatPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);
        addButtonFunctionality();
        Animation.fadeIn(findViewById(R.id.back), 450);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
        Animation.fadeOut(findViewById(R.id.back), 100);
    }

    private void addButtonFunctionality() {
        auth=FirebaseAuth.getInstance();
        ImageView backBtn = findViewById(R.id.back);
        ImageView signupBtn = findViewById(R.id.signupBtn);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);

        signupBtn.setOnClickListener(v -> signup(signupBtn));

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });
    }

    private void signup(ImageView signupBtn) {
        signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
        signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));

        String emailAddressString = emailAddress.getText().toString();
        String passwordString = password.getText().toString();
        String repeatPasswordString = repeatPassword.getText().toString();

        boolean valid = true;

        if(Credentials.isEmpty(this, emailAddress, password, repeatPassword)) {
            return;
        }

        if(!Credentials.validEmail(emailAddressString)) {
            valid = false;
            emailAddress.setError("Incorrect email format");
            errorInputBox(emailAddress, this);
        }
        else validInputBox(emailAddress, this);

        if(passwordString.length() <= 5) {
            valid = false;
            password.setError("Password too short");
            errorInputBox(password, this);
        }
        else validInputBox(password, this);

        if(!passwordString.equals(repeatPasswordString)) {
            valid = false;
            repeatPassword.setError("Passwords do not match");
            errorInputBox(repeatPassword, this);
        }
        else validInputBox(repeatPassword, this);

        if(!valid || emailAddress.getError() != null)
            return;

        checkEmailExistsOrNot(emailAddressString);

    }

    private void continueSignup() {
        InformatieCont.username2 = emailAddress.getText().toString();
        InformatieCont.password2 = password.getText().toString();
//        SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
//        editor.putString(InformatieCont.username, emailAddress.getText().toString());
//        editor.putString(InformatieCont.password, password.getText().toString());
//        editor.apply();
        Intent intent = new Intent(this, SignUp2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }

    private void checkEmailExistsOrNot(String emailAddressString){

        auth.fetchSignInMethodsForEmail(emailAddressString).addOnCompleteListener(task -> {
            Log.d(TAG,""+ Objects.requireNonNull(task.getResult().getSignInMethods()).size());
            if (task.getResult().getSignInMethods().size() == 0){
                emailAddress.setError(null);
                validInputBox(emailAddress, getApplicationContext());
                continueSignup();

            } else {
                emailAddress.setError("Email is already in use");
                errorInputBox(emailAddress, getApplicationContext());
            }

        }).addOnFailureListener(Throwable::printStackTrace);
    }
}
