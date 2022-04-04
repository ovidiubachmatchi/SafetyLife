package com.protect.safetylife.controller.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.controller.login.LogInActivity;
import com.protect.safetylife.utils.Credentials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp1Activity extends AppCompatActivity {
    private EditText emailAddress;
    private  EditText password;
    private EditText repeatPassword;
    private FirebaseAuth auth;
    private FirebaseUser user;
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
        ImageView backBtn = findViewById(R.id.back);
        ImageView signupBtn = findViewById(R.id.signupBtn);
         emailAddress = findViewById(R.id.email);
         password = findViewById(R.id.password);
         repeatPassword = findViewById(R.id.repeatPassword);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            if (validCredentials()) {
                if(validEmail(emailAddress.getText().toString())) {
                    if(password.getText().toString().length()>5) {
                        if(password.getText().toString().equals(repeatPassword.getText().toString())) {
                            //registartion();
                            checkEmailExist();
                            SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                            editor.putString(InformatieCont.username, emailAddress.getText().toString());
                            editor.putString(InformatieCont.password, password.getText().toString());
                            editor.putString(InformatieCont.userId, auth.getCurrentUser().getUid());
                            editor.commit();
                            Intent intent = new Intent(this, SignUp2Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                        }
                        else
                        {
                            Toast.makeText(SignUp1Activity.this, "Password and repeat password do not match!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUp1Activity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    emailAddress.setError("Enter corect email!");
                    Toast.makeText(SignUp1Activity.this, "Email incorect!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });
    }

    private boolean validCredentials() {

        boolean valid = Credentials.isEmpty(this, emailAddress, password, repeatPassword);
        return !valid;
    }
    private boolean validEmail(String email)
    {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void checkEmailExist()
    {
        auth.fetchSignInMethodsForEmail(emailAddress.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            Toast.makeText(SignUp1Activity.this, "Email valid!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUp1Activity.this, "Email used!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
