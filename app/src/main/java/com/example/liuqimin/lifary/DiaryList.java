package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class DiaryList extends Activity implements ValueEventListener{
    Firebase rootRef;
    ArrayList<String> list;
    ArrayList<Diary> diaryList;
  //  MyCustomApapter adapter;
    GridView gView;
    boolean isSet = false;
    String userid;

    ImageButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        //generate list
        list = new ArrayList<String>();
        diaryList = new ArrayList<Diary>();
        Log.d("Lifary", "DiaryList: is created");
        isSet = false;
        userid = "0";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                userid = extras.getString("USER_ID");
                //      MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
                //       user = myDBHandler.findUserByID(userID);
                Log.d("Lifary", "UserProfile: userID = " + userid);
            }
        }

        editButton = (ImageButton) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), EditDiaryActivity.class);
                i.putExtra("USER_ID", userid);
                startActivity(i);
            }
        });

        Firebase.setAndroidContext(this);

        rootRef = new Firebase("https://liuqimintest.firebaseio.com/");
        String sid = userid;
        Firebase ref= rootRef.child(sid).child("diary");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot2) {
                int uploadCounter = (int) snapshot2.getChildrenCount();
                Log.d("Lifary", "DiaryList: uploadCounter = " + uploadCounter);
                if (!isSet) {
                    Log.d("Lifary", "Enter for loop");
                    for (DataSnapshot postSnapshot : snapshot2.getChildren()) {
                        DiaryHelper dh = postSnapshot.getValue(DiaryHelper.class);
                        Diary d = new Diary(0);
                        d.convert(dh);
                        d.print();
                        diaryList.add(d);
                        list.add(d.getDate());
                    }
                    isSet = true;

                    Log.d("Lifary", "DiaryList: list.size = " + list.size());
                    ArrayList<String>temp = list;
                    Collections.reverse(temp);
                    final ArrayList<Diary> d_temp = diaryList;
                    Collections.reverse(d_temp);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, temp);
                    MyCustomApapter myAdapter = new MyCustomApapter(getBaseContext(), android.R.layout.simple_list_item_1, diaryList);
                    myAdapter.setUserid(userid);

                    gView = (GridView) findViewById(R.id.gridView1);
                    gView.setAdapter(myAdapter);
                    gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Log.d("Lifary", "DiaryList: Click Item "+ position);
                            Diary d = d_temp.get(position);
                            int d_id = d.getId();
                            Log.d("Lifary", "DiaryList: d_id = " + d_id);
                            Intent i = new Intent(view.getContext(), DiaryView.class);
                            Log.d("Lifary", "DiaryList: position = " + position);
                            Log.d("Lifary", "DiaryList: id = " + d_id);
                            i.putExtra("DIARY_ID", d_id);
                            i.putExtra("USER_ID", userid);
                            startActivity(i);

                        }
                    });
                    Log.d("Lifary", "DiaryList: diary list is added ");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Lifary", "DiaryList: The read failed: " + firebaseError.getMessage());
            }
        });


        for(int i = 0; i < list.size(); i++){
            Log.d("Arrr", "list["+i+"] = " + list.get(i));
        }



  //      rootRef.addValueEventener(this);

            //instantiate custom adapter
    //    adapter = new MyCustomApapter(list, this);
        //handle listview and assign adapter
       // ListView lView = (ListView)findViewById(R.id.diaryListView);


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
    public void onDataChange(DataSnapshot dataSnapshot) {
   /*     int i = 0;
        if(isSet == false) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                //    Log.d("Lifary", "SignUp: there are " + i + " diaries");
                Firebase ref = postSnapshot.getRef();
                String s = ref.getPath().toString();
                    list.add(s);
                ArrayList<String> temp = new ArrayList<String>(list);
                Collections.reverse(temp);
                adapter = new MyCustomApapter(temp, this);
                gView.setAdapter(adapter);
                i++;

            }

            isSet = true;
        }
        Log.d("Lifary", "DiaryList:  list size = " + list.size());*/
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
  //      Log.d("Lifary","SignUp: The read failed: " + firebaseError.getMessage());
    }


}
