package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowContactActivity extends AppCompatActivity {

    public static TextView name;
    private ContactsDbAdapter db;
    private static ArrayList<String> listContacts;
    private ArrayAdapter<String> aa;
    public static ListView list;
    public static Button call;
    public static Button localize;
    public static Button message;
    public static Button mail;

    private void fillData() {
        // Get  the contact by idw from the database and create the item list

        long id = getIntent().getLongExtra("id",38);

        Cursor c = db.fetchContact(id);
        startManagingCursor(c);

    String[] from = new String[] {ContactsDbAdapter.KEY_NAME,ContactsDbAdapter.KEY_FIRSTNAME,ContactsDbAdapter.KEY_PHONE,ContactsDbAdapter.KEY_EMAIL,ContactsDbAdapter.KEY_ADDRESS};
        int[] to = new int[] { R.id.name,R.id.firstname,R.id.address,R.id.email,R.id.phone};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contacts = new SimpleCursorAdapter(this, R.layout.activity_list_contact, c, from, to);
        list.setAdapter(contacts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        Intent i = getIntent();
        String id = i.getStringExtra(MainActivity.EXTRA_MESSAGE);

        name = findViewById(R.id.name);
        name.setText(id);

        list = findViewById(R.id.list);
        localize = findViewById(R.id.localize);
        call = findViewById(R.id.call);
        message = findViewById(R.id.message);
        mail = findViewById(R.id.mail);

        listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, listContacts);
        db = new ContactsDbAdapter(this);
        db.open();
        fillData();


        localize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri location = Uri.parse("geo:0,0?q="+R.id.address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
                startActivity(intent);
            }
        });


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri location = Uri.parse("geo:0,0?q="+R.id.address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri location = Uri.parse("geo:0,0?q="+R.id.address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });
    }
}
