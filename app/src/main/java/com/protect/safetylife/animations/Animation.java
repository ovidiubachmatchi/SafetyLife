package com.protect.safetylife.animations;

import android.view.View;

public class Animation {

    public static void fadeInAfterDuration(View view, int duration) {
        view.setAlpha(0f);
        view.postDelayed(() -> view.animate()
                .alpha(1.0f), duration);
    }
    public static void fadeOut(View view, int duration) {
        view.animate().alpha(0.0f).setDuration(duration);
    }

}
