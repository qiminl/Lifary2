package com.example.liuqimin.lifary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Local SQLite helper class
 * Created by User on 2015/8/1.
 */
public class DiaryDBHandler extends SQLiteOpenHelper {

    private static final String DEBUG = "Lifary";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "diaryDB.db";
    private static final String TABLE_NAME = "diary";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_LATITUDE = "latitude";

    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_SHARE = "share";
    public static final String COLUMN_IMAGE = "img";
    public static final String COLUMN_IMAGE_URL = "imageurl";

    public static final String COLUMN_SOUND = "sound";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_IMAGE_URI = "imageUri";

    /**
    * diary format:
    *           0:COLUMN_ID,TEXT PRIMARY KEY 1:COLUMN_TEXT,TEXT 2:COLUMN_USERID,TEXT
    *           3:COLUMN_LATITUDE,REAL 4:COLUMN_LONGITUDE,REAL 5:COLUMN_SHARE,INTEGER
    *           6:COLOMN_DATE,TEXT 7:COLUMN_IMAGE_URL,TEXT 8:COLUMN_IMAGE,BLOB
    *           9:COLUMN_SOUND,BLOB 10:COLUMN_IMAGE_URI,TEXT
    * */
    public DiaryDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("context: " + context.toString());
    }
    //@todo I suppose this is to manually update db && curse around
    public DiaryDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    public DiaryDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME +"'");
        final String CREATE_DIARY_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ("
                + COLUMN_ID + " TEXT PRIMARY KEY," + COLUMN_TEXT+ " TEXT,"
                + COLUMN_USERID + " TEXT, "+COLUMN_LATITUDE + " REAL, "
                + COLUMN_LONGITUDE +" REAL,"+ COLUMN_SHARE + " INTEGER,"
                + COLUMN_DATE + " TEXT, " + COLUMN_IMAGE_URL +" TEXT,"
                + COLUMN_IMAGE+" BLOB,"+ COLUMN_SOUND + " BLOB, "
                + COLUMN_IMAGE_URI + " TEXT" + " )";
        db.execSQL(CREATE_DIARY_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }
    public long addDiary(Diary diary){

       //acquire access of writing to db
       SQLiteDatabase db = this.getWritableDatabase();
       String selectQuery = "SELECT * FROM " +  TABLE_NAME + " WHERE "
               + COLUMN_ID + " = " +diary.getId();

       Cursor cursor = db.rawQuery(selectQuery, null);
       long newRowId;

       /**
        * @todo handle the stupid duplicate: practice DRAFT !!!!!!
        * should test for difference
        */
       if(cursor.getCount()==1){
           //now only return the exist diary.
           newRowId = cursor.getColumnCount();
       }
       else{
           ContentValues values = new ContentValues();
           values.put(COLUMN_ID, diary.getId());
           values.put(COLUMN_DATE, diary.getDate());
           values.put(COLUMN_TEXT, diary.getContent());
           values.put(COLUMN_IMAGE, diary.getImg());
           values.put(COLUMN_IMAGE_URL, diary.getImageUrl());
           values.put(COLUMN_SOUND, diary.getAudio());
           values.put(COLUMN_LATITUDE, diary.getLatitude());
           values.put(COLUMN_LONGITUDE, diary.getLongitude());
           values.put(COLUMN_SHARE, diary.getShare());
           values.put(COLUMN_USERID,diary.getUserid());
           values.put(COLUMN_IMAGE_URI,diary.getImageUri());
           //System.out.println("db path: " + db.getPath());
           newRowId  = db.insert(TABLE_NAME, null, values);
           System.out.println("row id = " + newRowId);
           //db.close();
           Log.d("Lifary", "Diary Database added");
       }
       db.close();
       return newRowId;
   }
    public Diary findDiaryByID(String id){
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = \"" + id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Diary diary = new Diary(0);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            diary.setId(cursor.getString(0));
            diary.setUserid(cursor.getString(2));
            diary.addContents(cursor.getString(1));
            diary.addLocation(cursor.getFloat(3), cursor.getFloat(4));
            diary.setShare(cursor.getInt(5));
            diary.setDate(cursor.getString(6));
            diary.setImageUrl(cursor.getString(7));
            diary.setImageByByte(cursor.getBlob(8));
            diary.setAudioByte(cursor.getBlob(9));
            diary.setImageUri(cursor.getString(10));
            cursor.close();
            Log.d("Lifary", "close");
        } else {
            diary = null;
        }
        db.close();
        return diary;
    }

    public Diary findDiaryByTime(String da){
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = \"" + da + "\"";

        Log.d("Lifary", "SQL db");
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Lifary", "SQL cursor");
        Cursor cursor = db.rawQuery(query, null);

        Log.d("Lifary", "SQL cursor done");
        Diary diary = new Diary(0);
        if ( cursor.moveToFirst()) {

            Log.d("Lifary","cursor success: ");
            cursor.moveToFirst();
            Log.d("Lifary", "moves to first: ");
            diary.setId(Integer.parseInt(cursor.getString(0)));

            Log.d("Lifary", "set id , id = " + diary.getId());
            diary.addContents(cursor.getString(1));
            Log.d("Lifary", "add contents, contents =   " + diary.getContent());
            diary.addLocation(cursor.getFloat(2), cursor.getFloat(3));
            Log.d("Lifary", "add location ");
            diary.setShare(cursor.getInt(4));
            Log.d("Lifary", "set share ");
            diary.setDate(cursor.getString(5));
            Log.d("Lifary", "set date , date == " + diary.getDate());
            diary.setImageByByte(cursor.getBlob(6));
            diary.setAudioByte(cursor.getBlob(7));
            cursor.close();
            Log.d("Lifary", "cursor close");
            } else {
                diary = null;
                Log.d(DEBUG, "cannot find diary by time");
            }
        db.close();
        return diary;
    }

    public boolean deleteDiary(int id){
       boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Diary d= new Diary(0);

        if (cursor.moveToFirst()) {
            do {
                d.setId(Integer.parseInt(cursor.getString(0)));
                db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(d.getId())});
                cursor.close();
                result = true;
            }while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    /***
     * This function is used to look up the specified amount of diaries
     * in Desc order of Written date
     * @param limit is the number of diary that we want
     * @param offset is the starting rows
     * @return Diaries Is list of diaries that fit the q.
     */
    public Diaries listOfDiary(int offset, int limit, String userid){
        //Log.d("sqlite", "loading diary");
        //select number of "limit" diary starting from "offset" in descending order of DATE
        final String query = "Select * FROM " + TABLE_NAME +" WHERE "
                + COLUMN_USERID + " = '" + userid
                +"' ORDER BY "+ COLUMN_DATE+ " DESC "
                +" LIMIT " + limit + " OFFSET " + offset;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Diary diary = new Diary(0);
        List<Diary> diaries = new ArrayList<>();
        Diaries result = new Diaries();
        /*0:COLUMN_ID, 1:COLUMN_TEXT, 2:COLUMN_USERID 3:COLUMN_LATITUDE,
        *4:COLUMN_LONGITUDE, 5:COLUMN_SHARE, 6:COLOMN_DATE, 7:COLUMN_IMAGE_URL,
        *8:COLUMN_IMAGE, 9:COLUMN_SOUND*/
        if (cursor.moveToFirst()) {
            //fill in the table with
            cursor.moveToFirst();
            while (!cursor.isAfterLast() ) {
                diary.setId(cursor.getString(0));
                diary.setUserid(cursor.getString(2));
                diary.addContents(cursor.getString(1));
                diary.addLocation(cursor.getFloat(3), cursor.getFloat(4));
                diary.setShare(cursor.getInt(5));
                diary.setDate(cursor.getString(6));
                diary.setImageUrl(cursor.getString(7));
                diary.setImageByByte(cursor.getBlob(8));
                diary.setAudioByte(cursor.getBlob(9));
                diary.setImageUri(cursor.getString(10));

                diaries.add(diary);
                cursor.moveToNext();
            }
            result.setDiaries(diaries);
        } else {
            result = null;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     *
     * @return int count, the number of entry for Diary in the current table
     */
    public int numberOfDiaries (String userId){
        /** select the entire table is stupid
        String query = "Select * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getColumnCount();
        */
        final String query_count = "SELECT COUNT (DISTINCT "+
                COLUMN_ID + ")" + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERID +
                " = '" + userId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        //db.rawQuery(SQL_STATEMENT, new String[] { loginname, loginpass });as selectionArgs
        Cursor cursor = db.rawQuery(query_count, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
