package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DiaryListActivity extends Activity implements AdapterView.OnItemClickListener {

    SessionManager sessionManager;
    DiaryDBHandler diaryDBHandler;
    private Diaries diaries;
    final static int NUMBER_PER_PAGE = 10;
    private int page; //number of pages
    private int numberOfDiaries;//number of diaries;
    private String userId;
    ListView lv;
    List<Diary> diaryList;
    StableArrayAdapter adapter;

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
        //retrive diaries.
        diaries = diaryDBHandler.listOfDiary(0,NUMBER_PER_PAGE,userId);
        Diary[] diary = diaries.loadDiaries();


        setContentView(R.layout.activity_diary_list);
        lv = (ListView) findViewById(R.id.dblist);
        diaryList = new ArrayList<>();
        for(Diary d :diary) {
            d.print();
            diaryList.add(d);
        }
        adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, diaryList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {

        final String item = (String) adapterView.getItemAtPosition(position);
        view.animate().setDuration(2000).alpha(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        diaryList.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
    }

    private class StableArrayAdapter extends ArrayAdapter<Diary> {
        HashMap< Integer, Diary> mIdMap = new HashMap< Integer, Diary>();
        private final Context context;

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<Diary> diary) {
            super(context, textViewResourceId, diary);
            this.context = context;
            for (int i = 0; i < diary.size(); ++i) {
                mIdMap.put(i, diary.get(i));
            }
        }
        /*
        @Override
        public long getItemId(int position) {
            Diary item = getItem(position);
            return mIdMap.get(item);
        }*/
        @Override
        public boolean hasStableIds() {
            return true;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.diary_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.diaryTextView1);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.diaryImageView1);

            Diary diary = mIdMap.get(position);
            textView.setText(diary.getDate());
            if(diary.hasImage()) {
                imageView.setImageBitmap(diary.getImgBitmap());
            }
            //MediaStore.Images.Media.insertImage(getContentResolver(), yourBitmap, yourTitle , yourDescription);

            return rowView;
        }

    }

}
