package com.protect.safetylife.controller.stealprotection;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.Informatii.InformatieCont;
import com.protect.safetylife.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StealActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private static Switch aSwitch;
    static Context context;
    private static List<SliderItem> list;
    public static final Object lock = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steal_protection);
        ViewPager2 viewPager2 = findViewById(R.id.viewPagerImageSlider);
        aSwitch = findViewById(R.id.switchSteal);
        context = this;

        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName compName = new ComponentName(this, AdminReceiver.class);
        if(!devicePolicyManager.isAdminActive(compName)) {
            aSwitch.setChecked(false);
        } else {
            InformatieCont.sharedPreferences = getSharedPreferences(InformatieCont.login, Context.MODE_PRIVATE);
            Intent cameraService = new Intent(this, CameraService.class);
            if (InformatieCont.sharedPreferences.contains(InformatieCont.steal) && InformatieCont.sharedPreferences.getString(InformatieCont.steal, "").equals("on")) {
                if (!isMyServiceRunning())
                    startService(cameraService);
                aSwitch.setChecked(true);
            } else {
                devicePolicyManager.removeActiveAdmin(compName);
                if (isMyServiceRunning())
                    stopService(cameraService);
                aSwitch.setChecked(false);
            }
        }

        Thread t = new Thread(){
            @Override
            public void run() {
                if(list.size() == 0)
                    viewPager2.setAdapter(new SliderAdapter(new ArrayList<SliderItem>(){{add(new SliderItem(R.drawable.purple));}}, viewPager2));
                else
                    viewPager2.setAdapter(new SliderAdapter(list, viewPager2));

                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer((page, position) -> {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                });

                viewPager2.setPageTransformer(compositePageTransformer);
            }
        };
        t.start();

        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                if(!devicePolicyManager.isAdminActive(compName)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    startActivityForResult(intent, 1);
                }
            } else{
                Intent cameraService = new Intent(this, CameraService.class);

                if (isMyServiceRunning())
                    stopService(cameraService);

                SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                editor.putString(InformatieCont.steal, "off");
                editor.apply();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent cameraService = new Intent(this, CameraService.class);

                if (!isMyServiceRunning())
                    startService(cameraService);

                SharedPreferences.Editor editor = InformatieCont.sharedPreferences.edit();
                editor.putString(InformatieCont.steal, "on");
                editor.apply();
            } else {
                aSwitch.setChecked(false);
                Toast.makeText(this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void generateList() {
        Thread t = new Thread() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("steal_photos")
                        .document(Objects.requireNonNull(user).getUid())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> stringList = (List<String>) document.get("photo_url");
                            list = new ArrayList<>();

                            for(String s: Objects.requireNonNull(stringList)){
                                System.out.println(s);
                                list.add(new SliderItem(s));
                            }

                            synchronized (StealActivity.lock) {
                                StealActivity.lock.notifyAll();
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });

                try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}

                db.terminate();
                db.clearPersistence();
            }
        };

        t.start();
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceIterator : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceIterator.service.getClassName() != null) {
                CameraService.class.getName();
                if (CameraService.class.getName().equals(serviceIterator.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
