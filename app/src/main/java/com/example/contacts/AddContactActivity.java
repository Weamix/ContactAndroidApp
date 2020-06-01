package com.example.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author VITSE Maxime
 *  @author DJAMAA Wassim
 */

public class AddContactActivity extends AppCompatActivity {

    // Database
    private ContactsDbAdapter db;

    // Elements of the page
    public static EditText name;
    public static EditText firstname;
    public static EditText phone;
    public static EditText email;
    public static EditText address;
    public static ImageView add_contact;
    public static ImageView back;

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

        // Db connection
        db = new ContactsDbAdapter(this);
        db.open();

        // Creating a contact
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking if firstname, lastname and number are NOT empty
                if(name.getText().toString().equals("") || firstname.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    add_contact.setEnabled(false); // block the button register

                    // Put the fields in red if they are empty
                    firstname.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    name.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    phone.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    if(phone.getText().toString().length() > 0){
                        phone.getBackground().clearColorFilter();
                    }
                    if(name.getText().toString().length() > 0){
                        name.getBackground().clearColorFilter();
                    }
                    if(firstname.getText().toString().length() > 0){
                        firstname.getBackground().clearColorFilter();
                    }

                    // Alertdialog to declare that a contact need a firstname,lastname and a phone number
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
                    TextView textView = new TextView(AddContactActivity.this);
                    textView.setTextSize(15);
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setText("\nVeuillez remplir les champs manquants : \n");
                    if(name.getText().toString().equals("")){
                        textView.append("\nPrénom ");
                    }
                    if(firstname.getText().toString().equals("")){
                        textView.append("\nNom ");
                    }
                    if(phone.getText().toString().equals("")){
                        textView.append("\nNuméro de télèphone");
                    }
                    builder.setCustomTitle(textView);

                    builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog = builder.show();
                }else {
                    db.createContact(name.getText().toString(),firstname.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString());
                    Intent i = new Intent(AddContactActivity.this, MainActivity.class);
                    startActivity(i);
                }
                add_contact.setEnabled(true);
            }
        });

        // Back to the previous intent
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
