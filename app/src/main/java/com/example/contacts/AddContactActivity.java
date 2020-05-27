package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        name = findViewById(R.id.emailTxt);
        firstname = findViewById(R.id.firstnameTxt);
        phone = findViewById(R.id.phoneTxt);
        email = findViewById(R.id.emailTxt);
        address = findViewById(R.id.address);

        add_contact = findViewById(R.id.add_contact);

        list = findViewById(R.id.list);
        listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContacts);
        db = new ContactsDbAdapter(this);
        db.open();

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
                    name.setText("");
                    firstname.setText("");
                    phone.setText("");
                    email.setText("");
                    address.setText("");
                    Intent i = new Intent(AddContactActivity.this, MainActivity.class);
                    startActivity(i);
                }
                add_contact.setEnabled(true);
            }
        });
    }
}
