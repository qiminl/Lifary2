package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URLEncoder;


public class UserProfileActivity extends Activity {


    TextView usrProfile;
    ImageView usrProtraite;
    ImageView qrCode;
    Button newDiaryButton;
    Button addFriendButton;
    Button diaryListButton;
    User user;
    private SessionManager session;
    MyDBHandler myDBHandler;
    DiaryDBHandler diaryDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());

        diaryDBHandler = new DiaryDBHandler(this, null, null, 1);
        //SQLiteDatabase db = diaryDBHandler.getWritableDatabase();
        //db.setVersion(2);
        //Log.d("sqlite", "version: = " + db.getVersion());
        //PRAGMA user_version = 2;
        setContentView(R.layout.activity_user_profile);
        usrProfile = (TextView) findViewById(R.id.usernameProfileText);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("la.droid.qr.scan");    // scan function
                i.putExtra( "la.droid.qr.complete" , true);
                try {
                    //start activity
                    startActivityForResult(i, 1);
                } catch(ActivityNotFoundException ex) {
                    // if QRcodeDroid is not installed, then install it.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=la.droid.qr"));
                    startActivity(intent);
                }
            }
        });

        qrCode = (ImageView) findViewById(R.id.QRcodeImg);
        int userID;
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                //@todo use unique id instead
                userID = extras.getInt("USER_ID");
                //@todo I forgot what is the factory in use for.
                myDBHandler = new MyDBHandler(this, null);
                user = myDBHandler.findUserByID(userID);
                usrProfile.setText(user.getUsername());
            }
        }
        String va = "";
        Log.d("http","trying to get all diaries");
        new get_diaries().execute(va,va,va);
        Log.d("http","get all diaries done");
        newDiaryButton = (Button) findViewById(R.id.newDiaryButton);
        newDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditDiaryActivity.class);
                startActivity(i);
            }
        });

        /**
         * starting diary list activity.
         * @todo decide if it needs from result or use service.
         */
        diaryListButton = (Button) findViewById(R.id.diaryListButton);
        diaryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DiaryListActivity.class);
                startActivity(i);
            }
        });


        try {
            String encodeUrl = URLEncoder.encode("" + user.getID());
            Intent i = new Intent("la.droid.qr.encode");
            i.putExtra("la.droid.qr.code", encodeUrl);
            i.putExtra("la.droid.qr.size", 200);
            i.putExtra("la.droid.qr.image", true);
            try {
                startActivityForResult(i, 0);
            }catch (ActivityNotFoundException ex){
                Log.d("Lifary", "UserProfile: startActivity ERROR: " + ex.getLocalizedMessage());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=la.droid.qr"));
                startActivity(intent);
            }
        }catch(Exception e){
            Log.d("Lifary", "UserProfile: encode QRcode ERROR: " + e.getLocalizedMessage());

        }
        /**
         * Intent sendIntent = new Intent(Intent.ACTION_SEND);
         ...

         // Always use string resources for UI text.
         // This says something like "Share this photo with"
         String title = getResources().getString(R.string.chooser_title);
         // Create intent to show the chooser dialog
         Intent chooser = Intent.createChooser(sendIntent, title);

         // Verify the original intent will resolve to at least one activity
         if (sendIntent.resolveActivity(getPackageManager()) != null) {
         startActivity(chooser);
         }
         */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
         */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( 0==requestCode && null!=data && data.getExtras()!=null ) {
            String qrcodeImgPath = data.getExtras().getString("la.droid.qr.result");
            Uri imgPath = Uri.fromFile(new File(qrcodeImgPath));
            qrCode.setImageURI(imgPath);
        }

        if( 1==requestCode && null!=data && data.getExtras()!=null ) {
            String result = data.getExtras().getString("la.droid.qr.result");
            int id = Integer.parseInt(result.toString());
            MyDBHandler dbHandler = new MyDBHandler(this, null);
            User friend = dbHandler.findUserByID(id);
            TextView friendText = (TextView ) findViewById(R.id.friendText);
            friendText.setText(friend.getUsername());
        }
    }
    /* simple phone content
    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    */
    private class get_diaries extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionWithPost con = new ConnectionWithPost();
                Diaries diaries = con.getUserDiaries(session.get_uniqe_id());
                Diary diary[] = diaries.loadDiaries();
                for (Diary result : diary){
                    diaryDBHandler.addDiary(result);
                }
            }catch (Exception e){
                e.printStackTrace();

                //todo handle error
                Log.d("http", "CONNECTION ERROR during get all diaries:  " + "\t" + e.getLocalizedMessage());
            }
            return null;
        }
    }
}
