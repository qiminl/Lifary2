package com.example.liuqimin.lifary;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by liuqi on 2015-10-12.
 */
public class MySqlHandler {

    private static int para;
    private String inputEmail;
    private String inputPassword;
    private SessionManager session;
    private MyDBHandler db;

    public void MySqlHandler(){para = 0;}

    public void DbConnect(String id, String pw){
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        //todo this session might have been abandoned
                        session.setLogin(true, "1");

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                    // Inserting row in users table
                        User u = new User();
                        db.addUser(u);

                    // Launch main activity
                        //Intent intent = new Intent();
                        //startActivity(intent);
                        //finish();
                    } else {
                        // Error in login. Get the error message
                        //String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(),
                                //errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: 2015-10-12 error message
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", inputEmail);
                params.put("password", inputPassword);

                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static boolean isConnected(int db){
        if (db>0)
            return true;
        else
            return false;
    };

}
