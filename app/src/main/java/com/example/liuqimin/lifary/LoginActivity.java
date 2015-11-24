package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class LoginActivity extends Activity implements View.OnClickListener{

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button signupPageButton;

    Firebase rootRef;
    ArrayList<User> userList;
    boolean isSet = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText) findViewById(R.id.usernameEdittext);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.logInButton);
        signupPageButton = (Button) findViewById(R.id.signUpPageButton);
        signupPageButton.setOnClickListener(this);


        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://liuqimintest.firebaseio.com/");

        isSet = false;

        userList = new ArrayList<User>();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!isSet) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String usr_uniqueid = (String) postSnapshot.child("uniqueid").getValue();
                        String user_name = (String) postSnapshot.child("username").getValue();
                        String user_password = (String) postSnapshot.child("password").getValue();
                        int user_id = 0;
                        User usr = new User(user_name, user_password);
                        usr.setUniqueid(usr_uniqueid);
                        usr.setID(user_id);
                        if(usr == null){
                            Log.d("Lifary", "Signup: user == null");
                        }
                        else{
                            userList.add(usr);
                        }
                    }
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v == loginButton){
                            //    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
                                String username = usernameEditText.getText().toString();
                             //   User user = myDBHandler.findUser(username);
                                if(findUserByUsername(username) != -1) {
                                    User user = userList.get(findUserByUsername(username));
                                    if (user.getPassword().equals(passwordEditText.getText().toString())) {
                                        Toast.makeText(getBaseContext(), "user matched", Toast.LENGTH_LONG).show();
                                        // goes to user profile activity
                                        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                                        i.putExtra("USER_ID", user.getUniqueid());
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(getBaseContext(), "password doesn't match", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(getBaseContext(), "username doesn't found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                    isSet = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onClick(View v) {
        if (v == signupPageButton){
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        }


    }

    public int findUserByUsername(String name){
        boolean result  = false;
        if(userList !=null){
            for (int i = 0; i < userList.size(); i++){
                Log.d("Lifary", "SignUp: username[" + i+"] = " + userList.get(i).getUsername());
                if(name.equals(userList.get(i).getUsername())){
                    result = true;
                    return  i;
                }
            }
        }
        return -1;
    }
}
