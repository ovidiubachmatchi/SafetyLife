package com.protect.safetylife.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class TimeHandler {
    private Context context;

    public TimeHandler(Context context)
    {
        this.context=context;
    }

    public void setTime(){
        Intent intent=new Intent(context,ExecutableServiceLocation.class);
        PendingIntent sender= PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(am!=null)
        {
            long after=20*5*1000;
            long every=20*5*1000;

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.CUPCAKE)
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP,after,every,sender);
            else
            {
                am.setRepeating(AlarmManager.ELAPSED_REALTIME,after,every,sender);
            }
        }
    }

    public void cancelTime()
    {
        Intent intent=new Intent(context,ExecutableServiceLocation.class);
        PendingIntent sender= PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(am!=null)
        {
           am.cancel(sender);
        }
    }
}
