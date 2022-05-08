package com.protect.safetylife.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Shader;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.controller.dashboard.LocationMenu;


import java.util.ArrayList;

public class ExecutableServiceLocation extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(LocationMenu.context!=null) {
            Location x=new Location();
        }
    }
}
