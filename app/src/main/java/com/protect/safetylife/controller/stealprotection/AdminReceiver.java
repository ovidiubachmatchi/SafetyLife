package com.protect.safetylife.controller.stealprotection;

import static android.content.ContentValues.TAG;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.protect.safetylife.controller.dashboard.DashboardActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class AdminReceiver extends DeviceAdminReceiver {
    static byte[] data;
    static Uri photoUri;
    static StorageReference storageReference;
    public static final Object lock = new Object();

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "Device admin: enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "Device admin: disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordFailed(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle user) {
        super.onPasswordFailed(context, intent);
        Toast.makeText(context, "Device admin: password failed", Toast.LENGTH_SHORT).show();
        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (DashboardActivity.context == null || mgr.getCurrentFailedPasswordAttempts() < 2)
            return;

        storageReference = FirebaseStorage.getInstance().getReference()
                .child("img_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg");

        Thread x = new Thread() {
            @Override
            public void run() {
                try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}

                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(exception -> System.out.println("Fail!")).addOnSuccessListener(taskSnapshot -> {
                    synchronized (AdminReceiver.lock) {
                        System.out.println("Success!");
                        AdminReceiver.lock.notifyAll();
                    }
                });

                try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}

                storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            synchronized (AdminReceiver.lock) {
                                photoUri = uri;
                                AdminReceiver.lock.notifyAll();
                            }
                        })
                        .addOnFailureListener(Throwable::printStackTrace);

                try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("steal_photos")
                        .document(Objects.requireNonNull(user).getUid()).update("photo_url", FieldValue.arrayUnion(photoUri.toString()))
                        .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e))
                        .addOnCompleteListener(task -> {
                            synchronized (AdminReceiver.lock) {
                                AdminReceiver.lock.notifyAll();
                            }
                        });

                try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}

                db.terminate();
                db.clearPersistence().addOnCompleteListener(task -> {
                    synchronized (AdminReceiver.lock) {
                        AdminReceiver.lock.notifyAll();
                    }
                });

                try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}

                StealActivity.generateList();
            }
        };

        x.start();
        PhotoManager.takePhoto(DashboardActivity.context, true);
    }

    @Override
    public void onPasswordSucceeded(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle user) {
        super.onPasswordSucceeded(context, intent, user);
    }
}
