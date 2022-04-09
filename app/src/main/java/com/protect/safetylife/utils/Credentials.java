package com.protect.safetylife.utils;

import static com.protect.safetylife.utils.Animation.errorInputBox;
import static com.protect.safetylife.utils.Animation.validInputBox;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Credentials {

    public static boolean validEmail(String email)
    {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

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
            if(view.getText().toString().trim().equalsIgnoreCase("")) {
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
