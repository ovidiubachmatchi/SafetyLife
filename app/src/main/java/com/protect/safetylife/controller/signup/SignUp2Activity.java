package com.protect.safetylife.controller.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;
import com.protect.safetylife.utils.Credentials;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp2Activity extends AppCompatActivity {
    private Spinner sex;
    private EditText firstName;
    private EditText lastName;
    private EditText date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);
        addButtonFunctionality();
        addInputFunctionality();
        Animation.fadeIn(findViewById(R.id.back), 450);
    }

    private void addInputFunctionality() {

        addDateOfBirthInputFunctionality();

        addSexInputFunctionality();
    }

    private void addSexInputFunctionality() {
        Spinner dropdown = findViewById(R.id.sex);
        String[] items = new String[]{"", "Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
            // override dropdown default value = ""
            // to be ignored
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v;
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        dropdown.setAdapter(adapter);
    }

    private void addDateOfBirthInputFunctionality() {
        final Calendar myCalendar= Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            String myFormat="MM/dd/yy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
            EditText datePicker = findViewById(R.id.dateInput);
            datePicker.setText(dateFormat.format(myCalendar.getTime()));
        };

        EditText datePicker = findViewById(R.id.dateInput);

        datePicker.setOnClickListener(v -> new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
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
        date = findViewById(R.id.dateInput);
        firstName=findViewById(R.id.firstname);
        lastName=findViewById(R.id.lastName);
        sex=findViewById(R.id.sex);

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            if (validCredentials()) {
                SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                editor.putString(InformatieCont.firstname, firstName.getText().toString());
                editor.putString(InformatieCont.lastname, lastName.getText().toString());
                editor.putString(InformatieCont.sex, sex.getSelectedItem().toString());
                editor.putString(InformatieCont.date,date.getText().toString());
                editor.commit();
                Intent intent = new Intent(this, SignUp3Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        backBtn.setOnClickListener(v -> {
            backBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press_animation));
            finish();
        });

    }

    private boolean validCredentials() {
        EditText firstName = findViewById(R.id.firstname);
        EditText lastName = findViewById(R.id.lastName);
        EditText dateOfBirth = findViewById(R.id.dateInput);
        Spinner sex = findViewById(R.id.sex);

        boolean valid;

        valid = Credentials.isEmpty(this, firstName, lastName, dateOfBirth);
        valid &= Credentials.isEmpty(this, sex);

        return !valid;
    }


}
