package com.findmysalon.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATA_BASE = "FIND_MY_SALON";

    private static final String USER_TABLE = "USER_TABLE";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_TYPE = "USER_TYPE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATA_BASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE "+USER_TABLE+" (" +
                ""+USER_EMAIL+" TEXT PRIMARY KEY, " +
                ""+USER_PASSWORD+" TEXT, "+
                ""+USER_TYPE+" TEXT)";

        db.execSQL(createUserTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //db.execSQL("DROP IF TABLE EXISTS "+USER_TABLE);

    }

    public boolean addUser (String email, String password, String type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_EMAIL, email);
        contentValues.put(USER_PASSWORD, password);
        contentValues.put(USER_TYPE, type);

        Log.d(TAG, "addUser: email="+email+" to "+USER_TABLE);

        long result = db.insert(USER_TABLE, null, contentValues);

        if (result == -1){
            return false;
        } else {
            return true;
        }

    }

    public boolean deleteUser () {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(USER_TABLE, null, null);

        if (result == -1){
            return false;
        } else {
            return true;
        }

    }

    public Cursor getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+USER_TABLE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
