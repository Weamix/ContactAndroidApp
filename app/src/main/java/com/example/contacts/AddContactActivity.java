package com.example.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    private ContactsDbAdapter db;
    public static EditText name;
    public static EditText firstname;
    public static EditText phone;
    public static EditText email;
    public static EditText address;
    public static ImageView add_contact;
    public static ImageView back;
    public static ListView list;
    private static ArrayList<String> listContacts;
    private ArrayAdapter<String> aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        name = findViewById(R.id.name);
        firstname = findViewById(R.id.firstname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.mail);
        address = findViewById(R.id.address);

        add_contact = findViewById(R.id.add_contact);
        back = findViewById(R.id.back2);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
                    builder.setMessage(R.string.dialog_infos).setTitle(R.string.infos);

                    builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog = builder.show();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
