package com.protect.safetylife.controller.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.protect.safetylife.R;
import com.protect.safetylife.utils.Animation;
import com.protect.safetylife.utils.ListViewAdapterCallContacts;
import com.protect.safetylife.utils.ListViewAdapterSmsContacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SosMenu extends AppCompatActivity {

    public static ListView listViewHistory;
    public static ArrayList<String> historyList;
    public static ListViewAdapterHistory adapterHistory;

    public static ListView listViewCallContacts;
    public static ArrayList<String> callContactsList;
    public static ListViewAdapterCallContacts adapterCallContacts;


    public static ImageView callContactsAddButton;
    public static EditText callContactsInput;

    public static ListView listViewSmsContacts;
    public static ArrayList<String> smsContactsList;
    public static ListViewAdapterSmsContacts adapterSmsContacts;

    public static ImageView smsContactsAddButton;
    public static EditText smsContactsInput;

    public static FirebaseFirestore dbSOSMENU;

    public static void deleteSmsContacts(int position) {
        smsContactsList.remove(position);
        listViewSmsContacts.setAdapter(adapterSmsContacts);
        addSmsContactToDatabase(smsContactsList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_menu);
        dbSOSMENU = FirebaseFirestore.getInstance();
        Animation.fadeIn(findViewById(R.id.arrow_up), 600);
        Animation.fadeOut(findViewById(R.id.settings_sos), 600);
        Animation.fadeOut(findViewById(R.id.profile_sos), 600);
        addButtonsFunctionality();
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        updateContacts();
        updateHistory();
    }

    private void updateContacts() {
        DocumentReference docRef = dbSOSMENU.collection("contacts").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists() && (document.getData().get("callContact") != "" || document.getData().get("callContacts") != null)) {
                    callContactsList.add((String) document.getData().get("callContact"));
                    listViewCallContacts.setAdapter(adapterCallContacts);
                    callContactsAddButton.setVisibility(View.INVISIBLE);
                    callContactsInput.setVisibility(View.INVISIBLE);
                }
                if (document.exists() &&  document.getData().get("smsContacts") != null) {
                    for (Object smsContact : (ArrayList) Objects.requireNonNull(document.getData().get("smsContacts"))) {
                        smsContactsList.add((String) smsContact);
                    }
                    listViewSmsContacts.setAdapter(adapterSmsContacts);
                    Log.d("data", String.valueOf(document.getData().get("smsContacts")));
                }
            } else {
                Log.d("getContact", "get failed with ", task.getException());
            }
        });
    }

    private void updateHistory() {
        listViewHistory = findViewById(R.id.sosHistory);
        historyList = new ArrayList<>();
        adapterHistory = new ListViewAdapterHistory(getApplicationContext(), historyList);
        DocumentReference docRef = dbSOSMENU.collection("soshistory").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists() && (document.getData().get("time") != "" || document.getData().get("time") != null)) {
                    for (Object smsContact : (ArrayList) Objects.requireNonNull(document.getData().get("time"))) {
                        historyList.add((String) smsContact);
                    }
                    Log.d("data-time", String.valueOf(document.getData().get("time")));
                    listViewHistory.setAdapter(adapterHistory);
                }
            } else {
                Log.d("getContact", "get failed with ", task.getException());
            }
        });
    }

    private void addButtonsFunctionality() {
        findViewById(R.id.arrow_up).setOnClickListener(v -> finish());
        setUpCallContactsListView();
        setUpSMSContactsListView();
    }

    private void setUpCallContactsListView() {
        callContactsAddButton = findViewById(R.id.adaugare_contact_call);
        callContactsInput = findViewById(R.id.input_contact_call);
        listViewCallContacts = findViewById(R.id.callContacts);
        callContactsList = new ArrayList<>();
        adapterCallContacts = new ListViewAdapterCallContacts(getApplicationContext(), callContactsList);
        callContactsAddButton.setOnClickListener(view -> {
            String text = callContactsInput.getText().toString();
            if(text.length() == 0)
               Toast.makeText(SosMenu.this, "Insert a phone number first", Toast.LENGTH_SHORT).show();
            else {
                addCallContactToDatabase(text);
                callContactsList.add(text);
                callContactsInput.setText("");
                listViewCallContacts.setAdapter(adapterCallContacts);
                callContactsAddButton.setVisibility(View.INVISIBLE);
                callContactsInput.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addCallContactToDatabase(String number) {
        Map<String, Object> callContact = new HashMap<>();
        callContact.put("callContact", number);

        dbSOSMENU.collection("contacts").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .update(callContact)
                .addOnSuccessListener(aVoid -> Log.d("callContact DB", "Successfully written!"))
                .addOnFailureListener(e -> Log.w("callContact DB", "Error writing document", e));
    }

    private static void addSmsContactToDatabase(ArrayList<String> numbers) {
        Map<String, Object> callContact = new HashMap<>();
        callContact.put("smsContacts", numbers);

        dbSOSMENU.collection("contacts").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .update(callContact)
                .addOnSuccessListener(aVoid -> Log.d("smsContacts DB", "Successfully written!"))
                .addOnFailureListener(e -> Log.w("smsContacts DB", "Error writing document", e));
    }

    public static void deleteCallContactToDatabase() {
        Map<String, Object> callContact = new HashMap<>();
        callContact.put("callContact", "");

        dbSOSMENU.collection("contacts").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .update(callContact)
                .addOnSuccessListener(aVoid -> Log.d("callContact DB", "Successfully written!"))
                .addOnFailureListener(e -> Log.w("callContact DB", "Error writing document", e));
    }


    private void setUpSMSContactsListView() {
        smsContactsAddButton = findViewById(R.id.adaugare_contact_sms);
        smsContactsInput = findViewById(R.id.input_contact_sms);
        listViewSmsContacts = findViewById(R.id.smsContacts);
        smsContactsList = new ArrayList<>();
        adapterSmsContacts = new ListViewAdapterSmsContacts(getApplicationContext(), smsContactsList);
        smsContactsAddButton.setOnClickListener(view -> {
            String text = smsContactsInput.getText().toString();
            if(text.length() == 0)
                Toast.makeText(SosMenu.this, "Insert a phone number first", Toast.LENGTH_SHORT).show();
            else {
                addSmsContactToDatabase(smsContactsList);
                smsContactsList.add(text);
                smsContactsInput.setText("");
                listViewSmsContacts.setAdapter(adapterSmsContacts);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Animation.fadeOut(findViewById(R.id.arrow_up), 0);
        Animation.fadeIn(findViewById(R.id.settings_sos), 0);
        Animation.fadeIn(findViewById(R.id.profile_sos), 0);
        overridePendingTransition(R.anim.slide_down_finish_foreground, R.anim.slide_down_finish_foreground);
    }
}