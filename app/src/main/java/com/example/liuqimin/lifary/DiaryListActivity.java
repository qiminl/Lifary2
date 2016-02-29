package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class DiaryListActivity extends Activity implements AdapterView.OnItemClickListener {

    SessionManager sessionManager;
    DiaryDBHandler diaryDBHandler;
    private Diaries diaries;
    final static int NUMBER_PER_PAGE = 10;
    private int page; //number of pages
    private int numberOfDiaries;//number of diaries;
    private String userId;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve unique user id from sharedPreferences;
        sessionManager = new SessionManager(this);
        if(sessionManager.isLoggedIn())
            userId = sessionManager.get_uniqe_id();
        else {
            //@todo what to do when is not logged in? return to log in page???? or offline mode? forResult?
            Intent intent = new Intent(DiaryListActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        //@todo where to get data? from server or local db? which one better?
        diaryDBHandler = new DiaryDBHandler(this, null, null, 1);
        numberOfDiaries = diaryDBHandler.numberOfDiaries(userId);
        Log.d("sqlite","numberOfDiaries: " +numberOfDiaries);
        double temp = (double)numberOfDiaries/NUMBER_PER_PAGE;
        if (temp > (int)temp)
            page = (int)temp +1;
        else
            page = (int)temp;

        setContentView(R.layout.activity_diary_list);
        lv = (ListView) findViewById(R.id.dblist);
        lv.setOnItemClickListener(this);

        //retrive diaries.
        diaries = diaryDBHandler.listOfDiary(0,NUMBER_PER_PAGE,userId);

        //@todo change to listview loader to implement actual view
       // Diaries diaries = diaryDBHandler.listOfDiary(0, NUMBER_PER_PAGE,);
        //String a = diaries.toString();
        //Log.d("sqlite","diaries: " +a);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
