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
    public final static String firstname="firstname";
    public final static String lastname="name";
    public final static String date="date";
    public final static String sex="sex";
    public final static String street="street";
    public final static String country="country";
    public final static String zipcode="zipcode";
    public static boolean verificareLogat()
    {
        if(sharedPreferences.contains(username))
        {
            return true;
        }
        return false;
    }
}
