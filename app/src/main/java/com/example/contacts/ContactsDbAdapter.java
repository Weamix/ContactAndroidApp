package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple contacts database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all contacts as well as
 * retrieve or modify a specific contact.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class ContactsDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";

    private static final String TAG = "ContactsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "name text not null, firstname text not null,phone text not null, email text null, address text null );";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ContactsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the contacts database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ContactsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new contact using the name, firstname, phone, email and adress provided. If the contact is
     * successfully created return the new rowId for that contact, otherwise return
     * a -1 to indicate failure.
     *
     * @param name the name of the contact
     * @param firstname the firstname of the contact
     * @param phone the phone of the contact
     * @param email the email of the contact
     * @param address the address of the contact
     * @return rowId or -1 if failed
     */
    public long createContact(String name, String firstname, String phone, String email, String address) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_FIRSTNAME, firstname);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_ADDRESS, address);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the contact with the given rowId
     *
     * @param rowId id of contact to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteContact(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void clear() {

        mDb.delete(DATABASE_TABLE, KEY_NAME + ">0", null);
    }

    /**
     * Return a Cursor over the list of all contacts in the database
     *
     * @return Cursor over all contacts
     */
    public Cursor fetchAllContacts() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_FIRSTNAME,KEY_PHONE,KEY_EMAIL,KEY_ADDRESS}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the contact that matches the given rowId
     *
     * @param rowId id of contact to retrieve
     * @return Cursor positioned to matching contact, if found
     * @throws SQLException if contact could not be found/retrieved
     */
    public Cursor fetchContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_FIRSTNAME,KEY_PHONE,KEY_EMAIL,KEY_ADDRESS}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the contact using the details provided. The contact to be updated is
     * specified using the rowId, and it is altered to use the name,firstname,phone,email and address values passed in
     *
     * @param rowId id of contact to update
     * @param name value to set contact name to
     * @param firstname value to set contact firstname to
     * @param phone value to set contact phone to
     * @param email value to set contact email to
     * @param address value to set contact address to
     * @return true if the contact was successfully updated, false otherwise
     */
    public boolean updateContact(long rowId, String name, String firstname, String phone, String email, String address) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_FIRSTNAME, firstname);
        args.put(KEY_PHONE, phone);
        args.put(KEY_EMAIL, email);
        args.put(KEY_ADDRESS, address);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
