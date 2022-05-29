package com.protect.safetylife.controller.stealprotection;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.DashboardActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StealActivity extends AppCompatActivity {
    private static List<SliderItem> list = new ArrayList<>();
    private static Context context;
    public static final Object lock = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        generateList();
        setContentView(R.layout.activity_steal_protection);
        ViewPager2 viewPager2 = findViewById(R.id.viewPagerImageSlider);

        Thread t = new Thread(){
            @Override
            public void run() {
                /*try { synchronized (lock) { lock.wait(); } }
                catch (InterruptedException ignored) {}*/

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
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
}
