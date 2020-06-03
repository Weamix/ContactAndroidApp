package com.example.contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.ArrayList;

/**
 * @author VITSE Maxime
 *  @author DJAMAA Wassim
 */

public class MainActivity extends AppCompatActivity {

    // Database
    private ContactsDbAdapter db;

    // Elements of the page
    private static ArrayList<String> listContacts;
    private ArrayAdapter<String> aa;
    public static ListView list;
    public static ListView favs;

    // Variables link to the c2 cursor to display data of one contact by id
    public String firstnameTxt;
    public String lastnameTxt;
    public String phoneTxt;
    public String emailTxt;
    public String addressTxt;

    public static ImageView add;

    // Get all of the contacts from the database and create the item list
    private void fillData() {
        Cursor c = db.fetchAllContacts();
        startManagingCursor(c);

        String[] from = new String[] {ContactsDbAdapter.KEY_NAME,ContactsDbAdapter.KEY_FIRSTNAME};
        int[] to = new int[] { R.id.name,R.id.firstname};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contacts = new SimpleCursorAdapter(this, R.layout.activity_list_contacts, c, from, to);
        list.setAdapter(contacts);

        fillDataFavorite();
    }

    private void fillDataFavorite() {
        Cursor cFav = db.fetchAllFavorites();
        startManagingCursor(cFav);

            String[] from = new String[]{ContactsDbAdapter.KEY_NAME, ContactsDbAdapter.KEY_FIRSTNAME};
            int[] to = new int[]{R.id.name, R.id.firstname};

            // Now create an array adapter and set it to display using our row
            SimpleCursorAdapter contacts = new SimpleCursorAdapter(this, R.layout.activity_list_contacts, cFav, from, to);
            favs.setAdapter(contacts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        list = findViewById(R.id.list);
        listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, listContacts);
        favs = findViewById(R.id.favs);

        // Db connection
        db = new ContactsDbAdapter(this);
        db.open();
        fillData();

        // Intent add contact
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(i);
            }
        });

        //Intent display informations of a specific contact
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ShowContactActivity.class);
                Bundle b = new Bundle();
                b.putLong("id", id); //Your id
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
                finish();
            }
        });

        favs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ShowContactActivity.class);
                Bundle b = new Bundle();
                b.putLong("id", id); //Your id
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
                finish();
            }
        });

        registerForContextMenu(list);
        registerForContextMenu(favs);
    }

    // Create Menu contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextuel, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor SelectedTaskCursor = (Cursor) list.getItemAtPosition(info.position);
        final long SelectedTask = SelectedTaskCursor.getLong(SelectedTaskCursor.getColumnIndex("_id"));

        // Get the data of a contact by id
        Cursor c2 = db.fetchContact(SelectedTask);
        startManagingCursor(c2);

        if(c2.moveToFirst()){
            do{
                firstnameTxt = c2.getString(c2.getColumnIndex("firstname"));
                lastnameTxt = c2.getString(c2.getColumnIndex("name"));
                phoneTxt = c2.getString(c2.getColumnIndex("phone"));
                emailTxt = c2.getString(c2.getColumnIndex("email"));
                addressTxt = c2.getString(c2.getColumnIndex("address"));

            }while (c2.moveToNext());
        }
        c2.close();

        switch (item.getItemId()) {
            // Menu contextual : Call
            case R.id.call :
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +phoneTxt));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;

            // Menu contextual : Message
            case R.id.message :
                Uri uri = Uri.parse("smsto:"+phoneTxt);
                Intent intentMessage = new Intent(Intent.ACTION_SENDTO, uri);
                intentMessage.putExtra("sms_body", "");
                startActivity(intentMessage);
                return true;

            // Menu contextual : GoogleMaps
            case R.id.localize :
                Uri location = Uri.parse("geo:0,0?q="+addressTxt);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
                return true;

            // Menu contextual : Mail
            case R.id.mail :
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",emailTxt, null));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTxt);
                startActivity(Intent.createChooser(emailIntent, ""));
                return true;

            // Menu contextual : Editing contact
            case R.id.edit:
                Intent i = new Intent(MainActivity.this, EditContactActivity.class);

                Bundle b = new Bundle();
                b.putLong("id", SelectedTask); //Your id
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
                finish();
                return true;

            // Menu contextual : Deleting contact
            case R.id.delete_contact :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                // Add the buttons on the AlertDialog
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteContact(SelectedTask);
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
                return true;

            // Menu contextual : Add contact in favorite
            case R.id.add_favorite:
                db.addFavorite(SelectedTask);
                fillData();
                return true;

            // Menu contextual : Delete contact in favorite
            case R.id.delete_favorite:
                db.deleteFavorite(SelectedTask);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
