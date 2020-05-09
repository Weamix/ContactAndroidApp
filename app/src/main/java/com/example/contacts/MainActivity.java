package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static Button button;
    private static ArrayList<String> listContacts;
    private ArrayAdapter<String> aa;
    public static ListView list;
    private ContactsDbAdapter db;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private void fillData() {
        // Get all of the contacts from the database and create the item list
        Cursor c = db.fetchAllContacts();
        startManagingCursor(c);

        String[] from = new String[] { ContactsDbAdapter.KEY_NAME,ContactsDbAdapter.KEY_FIRSTNAME};
        int[] to = new int[] { R.id.name,R.id.firstname };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contacts =new SimpleCursorAdapter(this, R.layout.activity_list_contacts, c, from, to);
        list.setAdapter(contacts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button3);
        list = findViewById(R.id.list);

        listContacts = new ArrayList<String>() ;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, listContacts);

        db = new ContactsDbAdapter(this);
        db.open();
        fillData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(i);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ShowContactActivity.class);
                TextView TextView = findViewById(R.id.name);
                String name = TextView.getText().toString();
                i.putExtra(EXTRA_MESSAGE, name);
                startActivity(i);
            }
        });

        registerForContextMenu(list);
    }

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

        switch (item.getItemId()) {
            case R.id.delete_contact :
                db.deleteContact(SelectedTask);
                fillData();
            default:
                return super.onContextItemSelected(item);
        }
    }
}
