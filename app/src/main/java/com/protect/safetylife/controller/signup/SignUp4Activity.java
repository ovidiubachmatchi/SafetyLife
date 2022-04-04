package com.protect.safetylife.controller.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.LandingActivity;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.controller.login.LogInActivity;
import com.protect.safetylife.utils.Animation;
import com.protect.safetylife.utils.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUp4Activity extends AppCompatActivity {
    public static ListView listView;
    public static ArrayList<String> boli;
    public static ListViewAdapter adapter;
    private ImageView addButton;
    private EditText dateInput;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup4);
        addButtonFunctionality();
        Animation.fadeIn(findViewById(R.id.back), 450);
        adaugareBoli();
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
        progressDialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();  // initializam instanta la baza de date
        user=auth.getCurrentUser();      // user curent logat
        db=FirebaseFirestore.getInstance();
        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            registartion();
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
           // registartion();
            //erase();
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });

    }

    /**
     * Adaugarea bolilor
     */
    public void adaugareBoli()
    {
        addButton=findViewById(R.id.adaugareBoli);
        dateInput=findViewById(R.id.dateInput);
        listView=findViewById(R.id.listaBoli);
        boli=new ArrayList<>();
        adapter=new ListViewAdapter(getApplicationContext(),boli);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=dateInput.getText().toString();
                if(text.length() == 0)
                {
                    Toast.makeText(SignUp4Activity.this, "Insert a disease!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    boli.add(text);
                    dateInput.setText("");
                    Toast.makeText(SignUp4Activity.this, "Added: "+text, Toast.LENGTH_SHORT).show();
                    listView.setAdapter(adapter);
                }
            }
        });
    }
    public static void stergreBoli(int i)
    {
        boli.remove(i);
        listView.setAdapter(adapter);
    }
    private String unireBoli()
    {
        String aux="";
        for(String s:boli)
            aux=aux+s+", ";
        return aux;
    }

    private void registartion()
    {

        auth.createUserWithEmailAndPassword(InformatieCont.sharedPreferences.getString(InformatieCont.username,""),InformatieCont.sharedPreferences.getString(InformatieCont.password,"")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    adaugareDateBaza();
                    Toast.makeText(SignUp4Activity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp4Activity.this, "Error try again later!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void adaugareDateBaza()
    {
        Map<String,Object> info=new HashMap<>();
        info.put("Email",InformatieCont.sharedPreferences.getString(InformatieCont.username,""));
        info.put("First Name",InformatieCont.sharedPreferences.getString(InformatieCont.firstname,""));
        info.put("Last Name", InformatieCont.sharedPreferences.getString(InformatieCont.lastname,""));
        info.put("Sex", InformatieCont.sharedPreferences.getString(InformatieCont.sex,""));
        info.put("Date of birth", InformatieCont.sharedPreferences.getString(InformatieCont.date,""));
        info.put("Country", InformatieCont.sharedPreferences.getString(InformatieCont.country,""));
        info.put("Street",InformatieCont.sharedPreferences.getString(InformatieCont.street,""));
        info.put("Zipcode",InformatieCont.sharedPreferences.getString(InformatieCont.zipcode,""));
        if(boli.size()==0)
            info.put("Disease","Without diseases");
        else
            info.put("Disease",unireBoli());
        db.collection("users").document(user.getUid()).set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignUp4Activity.this, "Data save successful!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp4Activity.this, "Error try again later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
