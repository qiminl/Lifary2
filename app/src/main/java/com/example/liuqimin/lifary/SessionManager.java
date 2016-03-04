package com.example.liuqimin.lifary;

/**
 * This Session manager user SharedPreference to manage user
 * Created by liuqi on 2015-10-12.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Login";

    private static final String KEY_IS_LOGIN = "isLoggedIn";
    private static final String UNIQUE_ID = "unique_id";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn, String unique_id) {

        editor.putBoolean(KEY_IS_LOGIN, isLoggedIn);
        if(isLoggedIn)
            editor.putString(UNIQUE_ID,unique_id );
        else
            editor.putString(UNIQUE_ID,"-1" );
        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public String get_uniqe_id(){
        return pref.getString(UNIQUE_ID,"-1");
    }
}
