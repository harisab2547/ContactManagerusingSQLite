package com.example.contactmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactmanager.R;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.ui.util;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context ) {
        super(context, util.DATABASE_NAME , null , util.DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACT_TABLE = "CREATE TABLE " + util.DATABASE_NAME  + " ( " + util.KEY_ID
                + " INTEGER PRIMARY KEY, " + util.KEY_NAME + " TEXT, " + util.KEY_PHONE_NUMBER +" TEXT "
                + ")";

        db.execSQL(CREATE_CONTACT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String dropTable =String.valueOf(R.string.dp_Drop) ;
                db.execSQL(dropTable, new String[]{util.DATABASE_NAME});
                onCreate(db);



    }
    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(util.KEY_NAME,contact.getName());
        contentValues.put(util.KEY_PHONE_NUMBER,contact.getPhoneNumber());
        db.insert(util.TABLE_NAME,null,contentValues);
        db.close();

    }
    public Contact getContact(int id){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(util.TABLE_NAME,new String[]{String.valueOf(util.KEY_ID), util.KEY_NAME,util.KEY_PHONE_NUMBER},
                util.KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhoneNumber(cursor.getString(2));
        return contact;



    }
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //Select all contacts
        String selectAll = "SELECT * FROM " + util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        //Loop through our data
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));

                //add contact objects to our list
                contactList.add(contact);
            }while (cursor.moveToNext());
        }

        return contactList;
    }
    public int updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(util.KEY_NAME,contact.getName());
        values.put(util.KEY_PHONE_NUMBER,contact.getPhoneNumber());

        return db.update(util.TABLE_NAME,values,util.KEY_ID +"=?", new String[]{
                String.valueOf(contact.getId())
        });

    }
    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(util.TABLE_NAME,util.KEY_ID + "=?" ,new String[]{
                String.valueOf(contact.getId())
        });
        db.close();

    }
    public int getCount(){
        String query = "SELECT * FROM " + util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{null});
        return cursor.getCount();
    }
}
