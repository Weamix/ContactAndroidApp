package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.util.Log;

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
    public String addresstest;

    private void fillData() {
        final TextView address = findViewById(R.id.addressTxt);

        // Get  the contact by idw from the database and create the item list

        long id = getIntent().getLongExtra("id",38);

        Cursor c = db.fetchContact(id);
        startManagingCursor(c);

        String[] from = new String[] {ContactsDbAdapter.KEY_NAME,ContactsDbAdapter.KEY_FIRSTNAME,ContactsDbAdapter.KEY_PHONE,ContactsDbAdapter.KEY_EMAIL,ContactsDbAdapter.KEY_ADDRESS};
        int[] to = new int[] { R.id.name,R.id.firstname,R.id.address,R.id.email,R.id.phone};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contacts = new SimpleCursorAdapter(this, R.layout.activity_list_contact, c, from, to);
        list.setAdapter(contacts);

        Cursor c2 = db.fetchContact(id);
        startManagingCursor(c2);

        //to be continued :D Remove list view elements by text view and fix intents
        if(c2.moveToFirst()){
            do{
                addresstest = c2.getString(c2.getColumnIndex("address"));
                //String varaible2 = cursor.getString(cursor.getColumnIndex("column_name2"));
                address.setText(c2.getString(c2.getColumnIndex("address")));

            }while (c2.moveToNext());
        }
        c2.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        Intent i = getIntent();
        String id = i.getStringExtra(MainActivity.EXTRA_MESSAGE);

        final TextView phone = findViewById(R.id.phone);


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
                db.open();
                fillData();
                Uri location = Uri.parse("geo:0,0?q="+addresstest);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +phone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", "default content");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Recipient"});
                email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                email.putExtra(Intent.EXTRA_TEXT   , "Message Body");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }
}
