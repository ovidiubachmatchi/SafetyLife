package com.protect.safetylife.Utils;

import android.content.Context;
import android.view.View;

import androidx.appcompat.content.res.AppCompatResources;

import com.protect.safetylife.R;

import java.util.Objects;

public class Utils {

    public static void fadeInAfterDuration(View view, int duration) {
        view.setAlpha(0f);
        view.postDelayed(() -> view.animate()
                .alpha(1.0f), duration);
    }
    public static void fadeOut(View view, int duration) {
        view.animate().alpha(0.0f).setDuration(duration);
    }

    public static void errorInputBox(View view, Context context) {
        if(view.getBackground().getConstantState().equals(Objects.requireNonNull(AppCompatResources.getDrawable(context, R.drawable.login_signup_gray_input_box)).getConstantState()))
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable.login_signup_error_gray_input_box));
    }

    public static void validInputBox(View view, Context context) {
        if(view.getBackground().getConstantState().equals(Objects.requireNonNull(AppCompatResources.getDrawable(context, R.drawable.login_signup_error_gray_input_box)).getConstantState()))
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable.login_signup_gray_input_box));
    }

}
