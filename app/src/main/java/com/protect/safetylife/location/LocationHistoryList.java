package com.protect.safetylife.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.LocationMenu;
import com.protect.safetylife.utils.ListViewAdapterLocationHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LocationHistoryList extends AppCompatActivity {


    public static ListView listView;
    public static ArrayList<String> ore;
    public static ListViewAdapterLocationHistory adapter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private TextView notFound;
    private EditText editTextDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history_list);

        listView=findViewById(R.id.listLocation);
        auth=FirebaseAuth.getInstance();  // initializam instanta la baza de date
        user=auth.getCurrentUser();      // user curent logat
        db=FirebaseFirestore.getInstance();
        notFound=findViewById(R.id.locationHistoryNotFound);
        editTextDate=findViewById(R.id.editTextDate);
        Date a=new Date();
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editTextDate.setText(dateFormat.format(a));
        ore=new ArrayList<>();
        adapter=new ListViewAdapterLocationHistory(getApplicationContext(),ore);
        listView.setAdapter(adapter);
        generateTime(dateFormat.format(a));
        addDateOfBirthInputFunctionality();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sessionIntent =new Intent(LocationHistoryList.this, MapsLocation.class);
                sessionIntent.putExtra("locatie",ore.get(position));
                startActivity(sessionIntent);
            }
    });
    }

    private void generateTime(String data)
    {

        db.collection("location_history").document(user.getUid()).collection(data)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            ore=new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData() +"  "+document.getId());
                                ore.add("Time:"+document.getId()+", "+document.getData().get("Address")+"\n,"+document.getData().get("Latitude")+","+document.getData().get("Longitude"));
                            }
                            if(ore.size()==0)
                            {
                                notFound.setText("There is no history!");
                                setList();
                            }
                            else
                            {
                                notFound.setText("");
                                setList();
                            }
                        } else {
                            System.out.println("nimic");
                        }
                    }
                });
    }
    private void setList()
    {
        adapter=new ListViewAdapterLocationHistory(getApplicationContext(),ore);
        listView.setAdapter(adapter);
    }
    private void addDateOfBirthInputFunctionality() {
        final Calendar myCalendar= Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            String myFormat="yyyy/MM/dd";
            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
            editTextDate.setText(dateFormat.format(myCalendar.getTime()));
            generateTime(dateFormat.format(myCalendar.getTime()));
        };

        editTextDate.setOnClickListener(v -> new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

}