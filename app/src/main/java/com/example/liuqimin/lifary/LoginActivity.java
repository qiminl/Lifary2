package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.util.HashMap;


public class LoginActivity extends Activity implements View.OnClickListener{

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button signupPageButton;
    private SessionManager session;
    private HashMap<String, String> map  = new HashMap<>();
    private String unique = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);/*
        if (session.isLoggedIn() && session.get_uniqe_id()!="-1"){
            Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
            i.putExtra("USER_ID", session.get_uniqe_id());
            startActivity(i);
        }
*/
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText) findViewById(R.id.usernameEdittext);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.logInButton);
        loginButton.setOnClickListener(this);
        signupPageButton = (Button) findViewById(R.id.signUpPageButton);
        signupPageButton.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());


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
        else if(v == loginButton){
            map.put("email",  usernameEditText.getText().toString());
            map.put("password", passwordEditText.getText().toString());
            String va = "";
            new Login().execute(va, va, va);

        }

    }

    private class Login extends AsyncTask<String, String, String> {
        HttpURLConnection urlc;
        int result = -1;

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionWithPost con = new ConnectionWithPost();
                //String respond = con.uploadDiary(map, getString(R.string.REGISTER));
                Message r = con.uploadDiary(map, getString(R.string.LOGIN), true);
                if(r.getSuccess() == "1"){
                    //Log.d("http", "respond:  " + r.getMessage());
                    //success = true;
                    unique = r.getMessage();
                    session.setLogin(true, unique);
                    Log.d("http", "session set, unique id = " + session.get_uniqe_id());
                    if (session.isLoggedIn()) {
                        // User is already logged in. Take him to main activity
                        Log.d("http", "is logged in: " + session.get_uniqe_id());

                        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(i);

                        finish();
                    }

                    return r.getMessage();
                }
                else
                    return "ERROR";
            }catch (Exception e){
                e.printStackTrace();
                Log.d("http","CONNECTION ERROR: " + "\t" + e.getLocalizedMessage());
            }
            return "ok";
        }
    }

}