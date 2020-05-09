package com.example.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    private ContactsDbAdapter db;
    public static EditText name;
    public static EditText firstname;
    public static EditText phone;
    public static EditText email;
    public static EditText address;
    public static Button add_contact;
    public static ListView list;
    private static ArrayList<String> listContacts;
    private ArrayAdapter<String> aa;

    /* inutile dans AddContactAcvitiy car on n'a pas besoin d'afficher toute la liste des contacts?

    private void fillData() {
        // Get all of the contacts from the database and create the item list
        Cursor c = db.fetchAllContacts();
        startManagingCursor(c);

        String[] from = new String[] { ContactsDbAdapter.KEY_NAME };
        int[] to = new int[] { R.id.name,R.id.firstname };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contacts =
                new SimpleCursorAdapter(this, R.layout.activity_list_contacts, c, from, to);
        list.setAdapter(contacts);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        name = findViewById(R.id.name);
        firstname = findViewById(R.id.firstname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);

        add_contact = findViewById(R.id.add_contact);

        list = findViewById(R.id.list);
        listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, listContacts);

        db = new ContactsDbAdapter(this);
        db.open();
        //fillData();

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listContacts.add(0, name.getText().toString());
                listContacts.add(1,firstname.getText().toString());
                db.createContact(name.getText().toString(),firstname.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString());
                name.setText("");
                firstname.setText("");
                phone.setText("");
                email.setText("");
                address.setText("");
                //fillData();
            }
        });

    }
}
