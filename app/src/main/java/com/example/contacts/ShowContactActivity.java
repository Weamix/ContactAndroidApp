package com.example.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowContactActivity extends AppCompatActivity {

    public static TextView name;
    private ContactsDbAdapter db;
    private static ArrayList<String> listContacts;
    private ArrayAdapter<String> aa;
    public static ListView list;
    public static ImageView call;
    public static ImageView localize;
    public static ImageView message;
    public static ImageView mail;
    public String firstnameTxt;
    public String lastnameTxt;
    public String phoneTxt;
    public String emailTxt;
    public String addressTxt;
    public static ImageView edit;
    public static ImageView back;
    public static ImageView delete;

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
        mail = findViewById(R.id.mail);

        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);


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

        edit.setOnClickListener(new View.OnClickListener() {
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowContactActivity.this);
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ShowContactActivity.this, MainActivity.class);
                        startActivity(intent);
                        long id1 = getIntent().getLongExtra("id",38);
                        db.deleteContact(id1);
                        fillData();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog = builder.show();
            }
        });

    }
}
