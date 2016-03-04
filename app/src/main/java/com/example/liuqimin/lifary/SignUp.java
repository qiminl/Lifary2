package com.example.liuqimin.lifary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.HashMap;


public class SignUp extends Activity implements View.OnClickListener{

    EditText usernameEditText;
    EditText passwordEditText;
    Button signUpButton;
    Button loginPageButton;
    EditText passwordConfirmEdit;
    private ProgressDialog pDialog;
    private SessionManager session;
    private HashMap<String, String> map  = new HashMap<>();
    private String unique = "";
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        /*
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            //todo change to main act; should change for every resume
            Intent intent = new Intent(SignUp.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
        usernameEditText = (EditText) findViewById(R.id.usernameEdittext);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);
        loginPageButton = (Button) findViewById(R.id.loginPageButton);
        loginPageButton.setOnClickListener(this);
        passwordConfirmEdit = (EditText) findViewById(R.id.passwordConfirmedEditText);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
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
        Log.d("http", "onclick");
        if(v == signUpButton){
            Log.d("http", "sign up button clicked");
            MyDBHandler myDBHandler = new MyDBHandler(this, null);
            //todo this is register
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if(password.equals(passwordConfirmEdit.getText().toString())) {
                map.put("email", username);
                map.put("name", "name");
                map.put("password", password);
                String va = " ";
                //Online DB test
                new Login().execute(va, va, va);

                Toast.makeText(this, "Account error, might already exist", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "please confirm your password", Toast.LENGTH_LONG).show();
            }

        }
        else if(v == loginPageButton){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        //into log in and Sign up page should log user out.
        session.setLogin(false, "-1");
    }

    private class Login extends AsyncTask<String, String, String> {
        HttpURLConnection urlc;
        int result = -1;

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionWithPost con = new ConnectionWithPost();
                //String respond = con.uploadDiary(map, getString(R.string.REGISTER));
                Message r = con.uploadDiary(map, getString(R.string.REGISTER), true);
                if(r.getSuccess().equals("1")){
                    //Log.d("http", "respond:  " + r.getMessage());
                    success = true;
                    unique = r.getMessage();
                    session.setLogin(true, unique);
                    Log.d("http", "session set, unique id = " + session.get_uniqe_id());
                    if (session.isLoggedIn()) {
                        // User is already logged in. Take him to main activity
                        //todo change to main act
                        Intent intent = new Intent(SignUp.this,
                                LoginActivity.class);
                        startActivity(intent);
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
        @Override
        protected void onPostExecute(String result) {
            if (session.isLoggedIn()) {
                Log.d("http", "you are in????");
            }
            else{
                //@todo handle more exceptions.
                Log.d("http", "you stupid shit enter the wrong info yo!");
                Toast.makeText(SignUp.this, "Wrong info, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
