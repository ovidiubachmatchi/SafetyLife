package com.protect.safetylife.Informatii;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class InformatieCont extends AppCompatActivity {
    public static SharedPreferences sharedPreferences ;
    public final static String login="login";
    public final static String username="username";
    public final static String password="password";
    public final static String userId="id";

    public static boolean verificareLogat()
    {
        if(sharedPreferences.contains(username))
        {
            return true;
        }
        return false;
    }
}
