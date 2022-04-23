package com.protect.safetylife.controller.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;

public class SosMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_menu);
        Animation.fadeIn(findViewById(R.id.arrow_up), 600);
        Animation.fadeOut(findViewById(R.id.settings_sos), 600);
        Animation.fadeOut(findViewById(R.id.profile_sos), 600);
        addButtonsFunctionality();
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }

    private void addButtonsFunctionality() {
        findViewById(R.id.arrow_up).setOnClickListener(v -> finish());
    }


    @Override
    public void finish() {
        super.finish();
        Animation.fadeOut(findViewById(R.id.arrow_up), 0);
        Animation.fadeIn(findViewById(R.id.settings_sos), 0);
        Animation.fadeIn(findViewById(R.id.profile_sos), 0);
        overridePendingTransition(R.anim.slide_down_finish_foreground, R.anim.slide_down_finish_foreground);
    }
}