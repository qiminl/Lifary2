package com.example.liuqimin.lifary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Local SQLite of users profile
 * Created by liuqimin on 15-07-04.
 */
public class MyDBHandler extends SQLiteOpenHelper {


    private static final String DEBUG = "Lifary";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    private static final String TABLE_USERS = "users";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_UPDATE_NUMBERS = "updateNumbers";

    public MyDBHandler(Context context,SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     * User format:
     *           0:COLUMN_ID,INTEGER UNIQUE PRIMARY KEY 1:COLUMN_USERNAME,TEXT
     *           2:COLUMN_PASSWORD,TEXT 3:COLUMN_UPDATE_NUMBERS,DOUBLE
     * */


    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo change to unique key
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXIST " +
                TABLE_USERS + "("
                + COLUMN_ID + " INTEGER UNIQUE PRIMARY KEY, " + COLUMN_USERNAME
                + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_UPDATE_NUMBERS + " DOUBLE " + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
        Log.d(DEBUG, "onCreate is called");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
        Log.d(DEBUG, "onUpgrade is called");
    }

    public void addUser(User user) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_ID, user.get_unique());
        //@todo pw should be encrypted
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_UPDATE_NUMBERS,user.getUpdateNumbers());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);

        db.close();
    }

    public User findUser(String username) {
        String query = "Select * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setID(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setUpdateNumbers(cursor.getString(3));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    public User findUserByID(int id){
        String query = "Select * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setID(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setUpdateNumbers(cursor.getString(3));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    public boolean deleteUser(String username) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            user.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_USERS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(user.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
