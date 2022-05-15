package com.protect.safetylife.utils;

import static com.protect.safetylife.controller.dashboard.SosMenu.adapterCallContacts;
import static com.protect.safetylife.controller.dashboard.SosMenu.callContactsAddButton;
import static com.protect.safetylife.controller.dashboard.SosMenu.callContactsInput;
import static com.protect.safetylife.controller.dashboard.SosMenu.listViewCallContacts;
import static com.protect.safetylife.controller.dashboard.SosMenu.callContactsList;
import static com.protect.safetylife.controller.dashboard.SosMenu.deleteCallContactToDatabase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.protect.safetylife.R;
import com.protect.safetylife.controller.dashboard.SosMenu;
import com.protect.safetylife.controller.signup.SignUp4Activity;

import java.util.ArrayList;

public class ListViewAdapterCallContacts extends ArrayAdapter<String> {
    private ArrayList<String> list;
    private Context context;


    public ListViewAdapterCallContacts(Context context, ArrayList<String> items)
    {
        super(context, R.layout.lista_boli,items);
        this.context=context;
        list=items;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layout = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layout.inflate(R.layout.lista_contacte_call,null);
            TextView numar = convertView.findViewById(R.id.numarCallContact);
            numar.setText(list.get(position));
            ImageView delete = convertView.findViewById(R.id.stergeCallContact);
//            delete.setOnClickListener(view -> SosMenu.stergereCallContact(position));
            delete.setOnClickListener(view -> {
                callContactsList.remove(position);
                listViewCallContacts.setAdapter(adapterCallContacts);
                callContactsAddButton.setVisibility(View.VISIBLE);
                callContactsInput.setVisibility(View.VISIBLE);
                deleteCallContactToDatabase();
            });
        }
        return convertView;
    }

}
