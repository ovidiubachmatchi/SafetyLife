package com.protect.safetylife.utils;

import static com.protect.safetylife.Informatii.InformatieCont.sharedPreferences;

import android.telephony.SmsManager;

import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.protect.safetylife.location.Location;

import java.util.ArrayList;
import java.util.List;

public class SMSService {

    public static void sendSMS(List<String> numereSMS) {

        ArrayList<String> s = Location.locationSend(DashboardActivity.context);
        String messageToSend =
                "SAFETY ALERT - "
                + sharedPreferences.getString("nume", "")
                + " "
                + sharedPreferences.getString("prenume", "")
               + " IS IN DANGER"+"\n"+
                "Latitudine:"+s.get(0)+"\n"+
                "Longitudine:"+s.get(1)+"\n"+
                "Tara:"+s.get(2)+"\n"
                +"Localitatea:"+s.get(3)+"\n"+
                "Adresa:"+s.get(4);


        for(String number : numereSMS)
            SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);
    }


}
