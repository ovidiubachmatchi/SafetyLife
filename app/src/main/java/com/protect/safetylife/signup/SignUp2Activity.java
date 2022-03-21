package com.protect.safetylife.signup;

import static com.protect.safetylife.Utils.Utils.errorInputBox;
import static com.protect.safetylife.Utils.Utils.validInputBox;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.protect.safetylife.R;
import com.protect.safetylife.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);
        addButtonFunctionality();
        addInputFunctionality();
        Utils.fadeInAfterDuration(findViewById(R.id.back), 450);
    }

    private void addInputFunctionality() {

        final Calendar myCalendar= Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel(myCalendar);
        };

        EditText datePicker = findViewById(R.id.dateOfBirth);

        MaterialDatePicker<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker()
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                        .build();

        datePicker.setOnClickListener(v -> new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());


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

    private void updateLabel(Calendar myCalendar){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        EditText datePicker = findViewById(R.id.dateOfBirth);
        datePicker.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_out_bottom, R.anim.back_slide_in_bottom);
        Utils.fadeOut(findViewById(R.id.back), 10);
    }

    private void addButtonFunctionality() {
        ImageView backBtn = findViewById(R.id.back);
        ImageView signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(v -> {
            signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageviewbutton));
            if (validCredentials()) {
                Intent intent = new Intent(this, SignUp3Activity.class);
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
        EditText firstName = findViewById(R.id.firstname);
        EditText lastName = findViewById(R.id.lastName);
        EditText dateOfBirth = findViewById(R.id.dateOfBirth);
        Spinner sex = findViewById(R.id.sex);

        boolean valid = true;

        if(TextUtils.isEmpty(firstName.getText().toString())) {
            errorInputBox(firstName, this);

            valid = false;
        }
        else {
            validInputBox(firstName, this);
        }

        if(TextUtils.isEmpty(lastName.getText().toString())) {
            errorInputBox(lastName, this);
            valid = false;
        }
        else {
            validInputBox(lastName, this);
        }

        if(TextUtils.isEmpty(dateOfBirth.getText().toString())) {
            errorInputBox(dateOfBirth, this);
            valid = false;
        }
        else {
            validInputBox(dateOfBirth, this);
        }

        if(TextUtils.isEmpty(String.valueOf(sex.getSelectedItem()))) {
            errorInputBox(sex, this);
            valid = false;
        }
        else {
            validInputBox(sex, this);
        }

        return valid;
    }


}
