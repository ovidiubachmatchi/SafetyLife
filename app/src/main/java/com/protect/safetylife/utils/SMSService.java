package com.protect.safetylife.utils;

import static com.protect.safetylife.Informatii.InformatieCont.sharedPreferences;

import android.telephony.SmsManager;

import com.protect.safetylife.Informatii.InformatieCont;

import java.util.List;

public class SMSService {

    public static void sendSMS(List<String> numereSMS) {

        // TODO SMS MESSAGE
        String messageToSend =
                "SAFETY ALERT - "
                + sharedPreferences.getString("nume", "")
                + " "
                + sharedPreferences.getString("prenume", "")
                + " IS IN DANGER";

        for(String number : numereSMS)
            SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);

    }


}
