package com.protect.safetylife.controller.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.utils.Animation;
import com.protect.safetylife.utils.Credentials;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {


    private FirebaseAuth auth; //pentru conectare in cont
    private ProgressDialog progres;  //pentru progresul la conectare
    /**
     *  Cu SharedPreferences se creaza pe dispozitiv fisiere care stocheaza datele pe care dorim sa
     *  fie reinoite la fiecare apel al aplicatiei
     *
     * @login , @username , @password  acestea vor fii cheile pentru a retine datele
     *  datle se retin in mod privat astfel nu o sa poata fi accesate de alte aplicatii sau decriptate
     * @id reprezinta id0ul unic din baza de date de la autentificare (User UID) va fii folosit
     * pentru firestore
     */
    private  EditText email;
    private  EditText parola;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        /*
         * Initializam informatiile din cont si verificam daca este logat
         */
        InformatieCont.sharedPreferences= getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
        if(InformatieCont.verificareLogat())
        {
            Intent activity2=new Intent(this, DashboardActivity.class);
            startActivity(activity2);
        }

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

        progres = new ProgressDialog(this);  //initializam progresul cu aceasta acctivitate
        auth=FirebaseAuth.getInstance();  // initializam instanta la baza de date
        email = findViewById(R.id.emailLogin);
        parola = findViewById(R.id.passwordLogin);
        loginBtn.setOnClickListener(v -> {
            loginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            if (!Credentials.isEmpty(this, email, parola)) {
                if (email.getText().toString().length() > 0 && parola.getText().toString().length() > 0) {
                    progres.setMessage("Please wait while we set up");
                    progres.setTitle("Login");
                    progres.setCanceledOnTouchOutside(false);
                    progres.show();
                    auth.signInWithEmailAndPassword(email.getText().toString(), parola.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                            editor.putString(InformatieCont.username, email.getText().toString());
                            editor.putString(InformatieCont.password, parola.getText().toString());
                            editor.putString(InformatieCont.userId, Objects.requireNonNull(auth.getCurrentUser()).getUid());
                            editor.apply();
                            progres.dismiss();
                            logat();
                            Toast.makeText(LogInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progres.dismiss();
                            email.setBackgroundResource(R.drawable.login_signup_error_gray_input_box);
                            parola.setBackgroundResource(R.drawable.login_signup_error_gray_input_box);
                            Toast.makeText(LogInActivity.this, "Login failed, email or password incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });

    }


    /**
     * Functia de logare pentru a deschide dashboard cand utili
     */
    private void logat()
    {
        Intent activity2=new Intent(this, DashboardActivity.class);
        startActivity(activity2);
    }

}
