package com.example.liuqimin.lifary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class SignUp extends Activity implements View.OnClickListener{

    EditText usernameEditText;
    EditText passwordEditText;
    Button signUpButton;
    Button loginPageButton;
    EditText passwordConfirmEdit;
    Firebase rootRef;
    double userCounter;
    String[] usernames;
    boolean isSet = false;

    ArrayList<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = (EditText) findViewById(R.id.usernameEdittext);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        loginPageButton = (Button) findViewById(R.id.loginPageButton);
        loginPageButton.setOnClickListener(this);
        passwordConfirmEdit = (EditText) findViewById(R.id.passwordConfirmedEditText);
        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://liuqimintest.firebaseio.com/");
        isSet = false;

        userList = new ArrayList<User>();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userCounter = dataSnapshot.getChildrenCount();
                Log.d("Lifary", "SignUp: there are " + userCounter + " users");
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
                    signUpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v == signUpButton){
                            //    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
                                String username = usernameEditText.getText().toString();

                                if(userList.size()>0 && findUserByUsername(username)){
                                    // search existing username
                                    Toast.makeText(getBaseContext(), "Sorry, username exists", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if(passwordEditText.getText().toString().equals(passwordConfirmEdit.getText().toString())) {
                                        User u = new User(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                                        try {
                                            Firebase ref = rootRef.push();
                                            Log.d("Lifary", "Signup: ref key ==== " + ref.getKey());
                                            u.setUniqueid(ref.getKey());
                                            ref.setValue(u);
                                            Log.d("Lifary", "Signup: user added");

                                            Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                                            i.putExtra("USER_ID", u.getUniqueid());
                                            startActivity(i);

                                        }catch (Exception e){
                                            Log.d("Lifary", "Signup: failed to add user to online database ERROR: " + e.getLocalizedMessage());
                                        }

                                    }
                                    else{
                                        Toast.makeText(getBaseContext(), "please confirm your password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });

                    isSet = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Lifary","SignUp: The read failed: " + firebaseError.getMessage());

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

        if(v == loginPageButton){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }

    public boolean findUserByUsername(String name){
        boolean result  = false;
        if(userList !=null){
            for (int i = 0; i < userList.size(); i++){
                Log.d("Lifary", "SignUp: username[" + i+"] = " + userList.get(i).getUsername());
                if(name.equals(userList.get(i).getUsername())){
                    result = true;
                    return  result;
                }
            }
        }
        return result;
    }
}
