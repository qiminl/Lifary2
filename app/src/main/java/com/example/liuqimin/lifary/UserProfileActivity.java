package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;


public class UserProfileActivity extends Activity {


    TextView usrProfile;
    ImageView qrCode;
    Button newDiaryButton;
    Button addFriendButton;
    User user;
    ArrayList<User> userList;

    Firebase rootRef;
    String userID;
    boolean isSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usrProfile = (TextView) findViewById(R.id.usernameProfileText);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        isSet = false;
        userList = new ArrayList<User>();

        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://liuqimintest.firebaseio.com/");
        // get scan function
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

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                userID = extras.getString("USER_ID");
          //      MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
         //       user = myDBHandler.findUserByID(userID);
                Log.d("Lifary", "UserProfile: userID = " + userID);

                rootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!isSet) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Log.d("Lifary", "UserProfile: postSnapshot.getKey() = " + postSnapshot.getKey());
                                String usr_uniqueid = (String) postSnapshot.child("uniqueid").getValue();
                                String user_name = (String) postSnapshot.child("username").getValue();
                                String user_password = (String) postSnapshot.child("password").getValue();
                                int user_id = 0;
                                User usr = new User(user_name, user_password);
                                usr.setUniqueid(usr_uniqueid);
                                usr.setID(user_id);
                                userList.add(usr);
                                if (postSnapshot.getKey().equals(userID)) {

                                    Log.d("Lifary", "UserProfile: found userID");
                                    String ur_uniqueid = (String) postSnapshot.child("uniqueid").getValue();
                                    String uer_name = (String) postSnapshot.child("username").getValue();
                                    String uer_password = (String) postSnapshot.child("password").getValue();
                                    int uer_id = 0;
                                    usr = new User(uer_name, uer_password);
                                    usr.setUniqueid(ur_uniqueid);
                                    usr.setID(uer_id);
                                    user = usr;
                                    Log.d("Lifary", "user uniqueID = " + user.getUniqueid()
                                            + "user name = " + user.getUsername());
                                    usrProfile.setText(user.getUsername());
                                    newDiaryButton = (Button) findViewById(R.id.newDiaryButton);
                                    newDiaryButton.setText("My Diaries");
                                    newDiaryButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(getApplicationContext(), DiaryList.class);
                                            i.putExtra("USER_ID", user.getUniqueid());
                                            startActivity(i);
                                        }
                                    });

                                    // Encode QRcode
                                    try {
                                        String encodeUrl = URLEncoder.encode(user.getUniqueid());
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

                                } else {
                                    Log.d("Lifary", "UserProfile: unable to find userID");
                                }
                            }
                            isSet = true;
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());

                    }
                });
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( 0==requestCode && null!=data && data.getExtras()!=null ) {
            //generates qrcode path
            String qrcodeImgPath = data.getExtras().getString("la.droid.qr.result");
            Uri imgPath = Uri.fromFile(new File(qrcodeImgPath));
            qrCode.setImageURI(imgPath);
        }

        if( 1==requestCode && null!=data && data.getExtras()!=null ) {
            //put result in la.droid.qr.result
            String result = data.getExtras().getString("la.droid.qr.result");
        //    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        //    User friend = dbHandler.findUserByID(id);
            if(findUserByUsername(result) != null) {
                User friend = findUserByUsername(result);
                TextView frientText = (TextView) findViewById(R.id.friendText);
                frientText.setText(friend.getUsername());
            }else {

            }
        }
    }

    public User findUserByUsername(String name){
        if(userList !=null){
            for (int i = 0; i < userList.size(); i++){
                if(name.equals(userList.get(i).getUniqueid())){
                    User u = userList.get(i);
                    return u;
                }
            }
        }
        return null;
    }
}
