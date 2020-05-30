package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    public String firstnameTxt;
    public String lastnameTxt;
    public String phoneTxt;
    public String emailTxt;
    public String addressTxt;
    public static Button button;
    public static Button buttonBack;

    private void fillData() {
        final TextView firstname = findViewById(R.id.firstnameTxt);
        final TextView lastname = findViewById(R.id.lastnameTxt);
        final TextView phone = findViewById(R.id.phoneTxt);
        final TextView email = findViewById(R.id.emailTxt);
        final TextView address = findViewById(R.id.addressTxt);

        // Get  the contact by idw from the database and create the item list

         long id = getIntent().getLongExtra("id",38);

        Cursor c = db.fetchContact(id);
        startManagingCursor(c);

        if(c.moveToFirst()){
            do{
                firstnameTxt = c.getString(c.getColumnIndex("firstname"));
                lastnameTxt = c.getString(c.getColumnIndex("name"));
                phoneTxt = c.getString(c.getColumnIndex("phone"));
                emailTxt = c.getString(c.getColumnIndex("email"));
                addressTxt = c.getString(c.getColumnIndex("address"));

                firstname.setText(c.getString(c.getColumnIndex("firstname")));
                lastname.setText(c.getString(c.getColumnIndex("name")));
                phone.setText(c.getString(c.getColumnIndex("phone")));
                email.setText(c.getString(c.getColumnIndex("email")));
                address.setText(c.getString(c.getColumnIndex("address")));

            }while (c.moveToNext());
        }
        c.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        /*Intent i = getIntent();
        final String id = i.getStringExtra(MainActivity.EXTRA_MESSAGE);

        list = findViewById(R.id.list);*/
        localize = findViewById(R.id.localize);
        call = findViewById(R.id.call);
        message = findViewById(R.id.message);
        mail = findViewById(R.id.name);

        button = findViewById(R.id.button);


        /*listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, listContacts);*/
        db = new ContactsDbAdapter(this);
        db.open();
        fillData();

        localize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*db.open();
                fillData();*/
                Uri location = Uri.parse("geo:0,0?q="+addressTxt);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +phoneTxt));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent sendIntent = new Intent(Intent.ACTION_VIEW);

                Uri uri = Uri.parse("smsto:"+phoneTxt);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",emailTxt, null));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTxt);
                startActivity(Intent.createChooser(emailIntent, ""));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowContactActivity.this, EditContactActivity.class);

                long id = getIntent().getLongExtra("id",38);

                Bundle b = new Bundle();
                b.putLong("id", id); //Your id
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
                finish();
            }
        });

        buttonBack = (Button) findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRetour = new Intent(ShowContactActivity.this, MainActivity.class);
                startActivity(intentRetour);
            }
        });
    }
}
