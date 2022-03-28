package com.protect.safetylife.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.protect.safetylife.R;

public class SosMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_menu);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_finish_foreground, R.anim.slide_down_finish_foreground);
    }
}