package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity {
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

        Intent i = getIntent();
        final String id = i.getStringExtra(MainActivity.EXTRA_MESSAGE);

        name = findViewById(R.id.nameEdit);
        firstname = findViewById(R.id.firstnameEdit);
        phone = findViewById(R.id.phoneEdit);
        email = findViewById(R.id.mailEdit);
        address = findViewById(R.id.addressEdit);

        add_contact = findViewById(R.id.add_contact);

        list = findViewById(R.id.list);
        listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContacts);

        db = new ContactsDbAdapter(this);
        db.open();
        fillData();

        name.setText(lastnameEdit);
        firstname.setText(firstnameEdit);
        phone.setText(phoneEdit);
        email.setText(emailEdit);
        address.setText(addressEdit);

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On teste si les champs nom,prenom ou le téléphone sont biens remplis (non vides)
                if(name.getText().toString().equals("") || firstname.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    add_contact.setEnabled(false); // bloque le bouton "Enregistrer"
                    //AlertDialog.Builder builder = new AlertDialog.Builder(this.);
                    //builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                }else {
                    listContacts.add(0, name.getText().toString());
                    listContacts.add(1,firstname.getText().toString());
                    db.createContact(name.getText().toString(),firstname.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString());
                    firstname.setText("");
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    address.setText("");
                    Intent i = new Intent(EditContactActivity.this, MainActivity.class);
                    startActivity(i);
                }
                add_contact.setEnabled(true);
            }
        });
    }
}
