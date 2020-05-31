package com.example.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*
 * @author VITSE Maxime
 * @author DJAMAA Wassim
 */

public class ShowContactActivity extends AppCompatActivity {

    private ContactsDbAdapter db;

    public static TextView name;

    public static ImageView edit;
    public static ImageView back;
    public static ImageView delete;

    public static ImageView call;
    public static ImageView localize;
    public static ImageView message;
    public static ImageView mail;

    public String firstnameTxt;
    public String lastnameTxt;
    public String phoneTxt;
    public String emailTxt;
    public String addressTxt;

    private void fillData() {
        final TextView firstname = findViewById(R.id.firstnameTxt);
        final TextView lastname = findViewById(R.id.lastnameTxt);
        final TextView phone = findViewById(R.id.phoneTxt);
        final TextView email = findViewById(R.id.emailTxt);
        final TextView address = findViewById(R.id.addressTxt);

        // Get  the contact by id from the database and create the item list

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

        localize = findViewById(R.id.localize);
        call = findViewById(R.id.call);
        message = findViewById(R.id.message);
        mail = findViewById(R.id.mail);

        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);

        // Db connection
        db = new ContactsDbAdapter(this);
        db.open();
        fillData();

        // Google Maps
        localize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri location = Uri.parse("geo:0,0?q="+addressTxt);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        // Call
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

        // Message
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+phoneTxt);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);

            }
        });

        // Mail
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",emailTxt, null));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTxt);
                startActivity(Intent.createChooser(emailIntent, ""));
            }
        });

        // Back to the previous intent
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Deleting contact
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

        // Updating a contact
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

    }
}
