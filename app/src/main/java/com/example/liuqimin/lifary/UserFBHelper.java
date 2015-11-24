package com.example.liuqimin.lifary;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.Firebase;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 2015/8/19.
 */
public class UserFBHelper {
    Firebase rootRef;

    UserFBHelper(String url, Context context) {
        Firebase.setAndroidContext(context);
        rootRef = new Firebase(url);
    }

    private class ReadUserData extends AsyncTask<String, Void, String> {


        Exception exception = null;

        Diary diary;

        public Diary getDiary() {
            return diary;
        }

        protected String doInBackground(String... urls) {
            return null;
        }

        protected void onPostExecute(String result) {

        }
    }
}
