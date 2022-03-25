package com.protect.safetylife.utils;

import static com.protect.safetylife.utils.Animation.errorInputBox;
import static com.protect.safetylife.utils.Animation.validInputBox;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

public class Credentials {

    /**
     *
     * @param context
     * @param views
     * @return
     */
    public static boolean areEmpty(Context context, EditText... views) {

        boolean valid = false;

        for(EditText view : views) {
            if(TextUtils.isEmpty(view.getText().toString())) {
                errorInputBox(view, context);
                valid = true;
            }
            else {
                validInputBox(view, context);
            }
        }

        return valid;
    }

}
