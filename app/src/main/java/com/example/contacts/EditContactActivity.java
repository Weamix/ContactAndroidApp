package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author VITSE Maxime
 * @author DJAMAA Wassim
 */

public class EditContactActivity extends AppCompatActivity {

    // Database
    private ContactsDbAdapter db;

    // Elements of the page
    public static EditText name;
    public static EditText firstname;
    public static EditText phone;
    public static EditText email;
    public static EditText address;
    public static ImageView add_contact;
    public static ImageView backedit;

    // Variables link to the cursor to display data of one contact by id
    public String firstnameEdit;
    public String lastnameEdit;
    public String phoneEdit;
    public String emailEdit;
    public String addressEdit;

    private void fillData() {
        final TextView firstname = findViewById(R.id.firstnameEdit);
        final TextView lastname = findViewById(R.id.nameEdit);
        final TextView phone = findViewById(R.id.phoneEdit);
        final TextView email = findViewById(R.id.mailEdit);
        final TextView address = findViewById(R.id.addressEdit);

        // Get  the contact by idw from the database and create the item list

        long id = getIntent().getLongExtra("id",18);

        Cursor c = db.fetchContact(id);
        startManagingCursor(c);

        if(c.moveToFirst()){
            do{
                firstnameEdit = c.getString(c.getColumnIndex("firstname"));
                lastnameEdit = c.getString(c.getColumnIndex("name"));
                phoneEdit = c.getString(c.getColumnIndex("phone"));
                emailEdit = c.getString(c.getColumnIndex("email"));
                addressEdit = c.getString(c.getColumnIndex("address"));

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
        setContentView(R.layout.activity_edit_contact);

        name = findViewById(R.id.nameEdit);
        firstname = findViewById(R.id.firstnameEdit);
        phone = findViewById(R.id.phoneEdit);
        email = findViewById(R.id.mailEdit);
        address = findViewById(R.id.addressEdit);

        add_contact = findViewById(R.id.add_contact);
        backedit = findViewById(R.id.backedit);

        // Db connection
        db = new ContactsDbAdapter(this);
        db.open();
        fillData();

        // Set data of the contact in each EditText
        name.setText(lastnameEdit);
        firstname.setText(firstnameEdit);
        phone.setText(phoneEdit);
        email.setText(emailEdit);
        address.setText(addressEdit);

        // Updating a contact by id
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking if firstname, lastname and number are NOT empty
                if(name.getText().toString().equals("") || firstname.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    add_contact.setEnabled(false); // bloque le bouton "Enregistrer"
                }else {
                    long id = getIntent().getLongExtra("id",18);
                    db.updateContact(id,name.getText().toString(),firstname.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString());
                    Intent i = new Intent(EditContactActivity.this, MainActivity.class);
                    startActivity(i);
                }
                add_contact.setEnabled(true);
            }
        });

        backedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = getIntent().getLongExtra("id",38);
                Intent intent = new Intent(EditContactActivity.this, ShowContactActivity.class);
                Bundle b = new Bundle();
                b.putLong("id", id); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });
    }
}
