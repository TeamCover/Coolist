package com.flipbox.cover.coolist.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Coolist";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Company table name
    private static final String TABLE_COMPANY = "company";

    // Role table name
    private static final String TABLE_ROLE = "role";

    // Status table name
    private static final String TABLE_STATUS = "status";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COMPANY = "company";

    //Company Table Columns names
    private static final String KEY_ID_COMPANY = "id";
    private static final String KEY_NAME_COMPANY = "name";
    private static final String KEY_ADDRESS_COMPANY = "address";
    private static final String KEY_TOKEN_COMPANY = "token";

    // ROle Table columns names
    private static final String KEY_ID_ROLE = "id";
    private static final String KEY_NAME_ROLE = "name";

    // Status Table Column names;
    private static final String KEY_ID_STATUS = "id";
    private static final String KEY_NAME_STATUS = "name";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COMPANY + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_COMPANY_TABLE = "CREATE TABLE " + TABLE_COMPANY + "("
                + KEY_ID_COMPANY + " INTEGER PRIMARY KEY," + KEY_NAME_COMPANY + " TEXT,"
                + KEY_ADDRESS_COMPANY + " TEXT,"
                + KEY_TOKEN_COMPANY + " TEXT" + ")";
        db.execSQL(CREATE_COMPANY_TABLE);

        String CREATE_ROLE_TABLE = "CREATE TABLE " + TABLE_ROLE + "("
                + KEY_ID_ROLE + " INTEGER PRIMARY KEY," + KEY_NAME_ROLE + " TEXT" + ")";
        db.execSQL(CREATE_ROLE_TABLE);

        String CREATE_STATUS_TABLE = "CREATE TABLE " + TABLE_STATUS + "("
                + KEY_ID_STATUS + " INTEGER PRIMARY KEY," + KEY_NAME_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_STATUS_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);

        // Create tables again
        onCreate(db);
    }

    public void addUser(int id, int company) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_COMPANY, company);


        // Inserting Row
        long co = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + co);
    }

    public void addCompany(int id, String name, String address, String token){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COMPANY,id);
        values.put(KEY_NAME_COMPANY, name);
        values.put(KEY_ADDRESS_COMPANY,address);
        values.put(KEY_TOKEN_COMPANY, token);

        long co = db.insert(TABLE_COMPANY,null, values);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + co);
    }

    public void addRole(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ROLE,id);
        values.put(KEY_NAME_ROLE, name);

        long co = db.insert(TABLE_ROLE,null, values);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + co);
    }

    public void addStatus(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_STATUS,id);
        values.put(KEY_NAME_STATUS,name);

        long co = db.insert(TABLE_STATUS,null, values);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + co);
    }

    /**
     * Getting user data from database
     * */
    public int getUserCompany() {
        int company = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            company = cursor.getInt(1);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + String.valueOf(company));

        return company;
    }

    public int getUserID(){
        int id = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return  id;
    }

    public String getCompanyByKey(int id){
        String name="";
        String selectQuery = "SELECT "+ KEY_NAME_COMPANY +" FROM " + TABLE_COMPANY + " WHERE "+KEY_ID_COMPANY+"="+String.valueOf(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            name = cursor.getString(1);
        }
        db.close();
        Log.d(TAG, "Fetching name from Sqlite: " + name);
        return name;
    }

    public String getRoleByKey(int id){
        String name="";
        String selectQuery = "SELECT "+ KEY_NAME_ROLE +" FROM " + TABLE_ROLE + " WHERE "+KEY_ID_ROLE+"="+String.valueOf(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            name = cursor.getString(1);
        }
        db.close();
        Log.d(TAG, "Fetching name from Sqlite: " + name);
        return name;
    }

    public String getStatusByKey(int id){
        String name="";
        String selectQuery = "SELECT "+ KEY_NAME_STATUS +" FROM " + TABLE_ROLE + " WHERE "+KEY_ID_STATUS+"="+String.valueOf(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            name = cursor.getString(1);
        }
        db.close();
        Log.d(TAG, "Fetching name from Sqlite: " + name);
        return name;
    }

    /**
     * Getting user login status return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.delete(TABLE_STATUS,null,null);
        db.delete(TABLE_COMPANY,null,null);
        db.delete(TABLE_ROLE,null,null);
        db.close();

        Log.d(TAG, "Deleted all info from sqlite");
    }

}
