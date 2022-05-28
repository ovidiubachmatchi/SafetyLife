package com.protect.safetylife.controller.stealprotection;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.protect.safetylife.controller.dashboard.DashboardActivity;


public class AdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "Device Admin : enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "Device Admin : disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordFailed(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle user) {
        super.onPasswordFailed(context, intent);
        Toast.makeText(context, "Device Admin : password_failed", Toast.LENGTH_SHORT).show();

        if (DashboardActivity.context != null)
            PhotoManager.takePhoto(DashboardActivity.context, true);
    }

    @Override
    public void onPasswordSucceeded(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle user) {
        super.onPasswordSucceeded(context, intent, user);
        Toast.makeText(context, "Device Admin : password_succeeded", Toast.LENGTH_SHORT).show();
    }
}
