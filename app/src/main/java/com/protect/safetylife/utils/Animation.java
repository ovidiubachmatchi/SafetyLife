package com.protect.safetylife.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.content.res.AppCompatResources;

import com.protect.safetylife.R;

import java.util.Objects;

public class Animation {

    public static void fadeInAfterDuration(View view, int duration) {
        view.setAlpha(0f);
        view.postDelayed(() -> view.animate()
                .alpha(1.0f), duration);
    }
    public static void fadeOut(View view, int duration) {
        view.animate().alpha(0.0f).setDuration(duration);
    }

    private static void shake(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake));
    }

    /**
     * Transform
     * @param view
     * @param context
     */
    public static void errorInputBox(View view, Context context) {
        if(view.getBackground().getConstantState().equals(Objects.requireNonNull(AppCompatResources.getDrawable(context, R.drawable.login_signup_gray_input_box)).getConstantState()))
            if(view.getWidth() < 400)
                view.setBackground(AppCompatResources.getDrawable(context, R.drawable.login_signup_error_gray_input_box_half));
            else
                view.setBackground(AppCompatResources.getDrawable(context, R.drawable.login_signup_error_gray_input_box));
        shake(view, context);
    }

    public static void validInputBox(View view, Context context) {
        if(view.getBackground().getConstantState().equals(Objects.requireNonNull(AppCompatResources.getDrawable(context, R.drawable.login_signup_error_gray_input_box)).getConstantState()))
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable.login_signup_gray_input_box));
    }

}
