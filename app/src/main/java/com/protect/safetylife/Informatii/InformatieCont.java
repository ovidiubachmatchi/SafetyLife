package com.protect.safetylife.Informatii;

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
    public final static String locationOn="locationOn";
    public final static String steal="steal";

    public  static String username2;
    public  static String password2;
    public  static String userId2;
    public  static String firstname2;
    public  static String lastname2;
    public  static String date2;
    public  static String sex2;
    public  static String street2;
    public  static String country2;
    public  static String zipcode2;

    public static boolean verificareLogat()
    {
        if(sharedPreferences.contains(username) || (username2!= null && !username2.isEmpty()))
        {
            return true;
        }
        return false;
    }
}
