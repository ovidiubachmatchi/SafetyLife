package com.protect.safetylife.controller.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.utils.Animation;
import com.protect.safetylife.utils.Credentials;

public class SignUp3Activity extends AppCompatActivity {

    private AutoCompleteTextView country;
    private EditText street;
    private EditText zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup3);
        addButtonFunctionality();
        Animation.fadeIn(findViewById(R.id.back), 450);
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
        street=findViewById(R.id.street);
        country=findViewById(R.id.country);
        zipcode=findViewById(R.id.zipcode);

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));

            if (validCredentials()) {
                InformatieCont.street2=street.getText().toString();
                InformatieCont.country2=country.getText().toString();
                InformatieCont.zipcode2=zipcode.getText().toString();

//                SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
//                editor.putString(InformatieCont.street, street.getText().toString());
//                editor.putString(InformatieCont.country, country.getText().toString());
//                editor.putString(InformatieCont.zipcode, zipcode.getText().toString());
//                editor.commit();
                Intent intent = new Intent(this, SignUp4Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });

        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        AutoCompleteTextView country = findViewById(R.id.country);
        country.setAdapter(countriesAdapter);

    }

    private boolean validCredentials() {
        EditText street = findViewById(R.id.street);
        EditText country = findViewById(R.id.country);
        EditText zipcode = findViewById(R.id.zipcode);

        boolean valid = Credentials.isEmpty(this, street, country, zipcode);

        return !valid;
    }

}
