package com.protect.safetylife.controller.stealprotection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;



public class PhotoManager {
    private static ProcessCameraProvider cameraProvider, cameraProviderService;
    private static ImageCapture imageCapture, imageCaptureService;

    public static void initializeCameraService(Context cameraContext, Context context, CameraSelector cameraSelector){
        if(cameraProviderService == null) {
            try {
                cameraProviderService = ProcessCameraProvider.getInstance(cameraContext).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA && cameraProviderService.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ||
                    cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA && cameraProviderService.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                cameraProviderService.unbindAll();
            }
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
        }

        imageCaptureService = new ImageCapture.Builder().build();
        cameraProviderService.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageCaptureService);
    }

    public static void initializeCamera(Context cameraContext, Context context, CameraSelector cameraSelector){
        if(cameraProvider == null) {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(cameraContext).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA && cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ||
                    cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA && cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                cameraProvider.unbindAll();
            }
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
        }

        imageCapture = new ImageCapture.Builder().build();
        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageCapture);
    }

    public static void takePhoto(Context context, Boolean service){
        ImageCapture camera;
        if(service)
            camera = imageCaptureService;
        else
            camera = imageCapture;

        /*ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "img_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg")).build();

        camera.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        System.out.println("Saved!");
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        System.out.println("Error!");
                    }
                });*/

        camera.takePicture(ContextCompat.getMainExecutor(context), new ImageCapture.OnImageCapturedCallback(){
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                synchronized (AdminReceiver.lock) {
                    System.out.println("Captured!");

                    Bitmap bitmap = convertImageProxyToBitmap(image);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    AdminReceiver.data = baos.toByteArray();
                    image.close();

                    AdminReceiver.lock.notifyAll();
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                super.onError(exception);
                System.out.println("Error!");
            }
        });
    }

    private static Bitmap convertImageProxyToBitmap(ImageProxy image) {
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byteBuffer.rewind();
        byte[] bytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }
}
