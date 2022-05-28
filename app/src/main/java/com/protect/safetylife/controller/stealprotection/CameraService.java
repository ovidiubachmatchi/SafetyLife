package com.protect.safetylife.controller.stealprotection;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.lifecycle.LifecycleService;

import com.protect.safetylife.controller.dashboard.DashboardActivity;

public class CameraService extends LifecycleService {

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        PhotoManager.initializeCameraService(DashboardActivity.context,this, CameraSelector.DEFAULT_FRONT_CAMERA);
        return super.onStartCommand(intent, flags, startId);
    }

}
