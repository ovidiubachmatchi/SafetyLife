package com.protect.safetylife.utils;

import static com.protect.safetylife.utils.Animation.errorInputBox;
import static com.protect.safetylife.utils.Animation.validInputBox;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;

public class Credentials {

    /**
     *  Check if multiple EditText are empty. The empty ones are going to change border in red and
     *  play a shaking animation.
     * @param context
     * @param editTexts one or even more EditText view
     * @return true if one of the input is empty otherwise false
     */
    public static boolean isEmpty(Context context, EditText... editTexts) {

        boolean valid = false;

        for(EditText view : editTexts) {
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
    /**
     *  Check if one or even more Spinner views are empty. The empty ones are going to change border in red and
     *  play a shaking animation.
     * @param context
     * @param spinners one or even more Spinner view
     * @return true if one of the input is empty otherwise false
     */
    public static boolean isEmpty(Context context, Spinner... spinners) {

        boolean valid = false;

        for(Spinner view : spinners) {
            if(TextUtils.isEmpty(String.valueOf(view.getSelectedItem()))) {
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
