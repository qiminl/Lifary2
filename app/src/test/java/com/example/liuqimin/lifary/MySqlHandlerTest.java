package com.example.liuqimin.lifary;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.liuqimin.lifary.MySqlHandler.isConnected;
import static org.junit.Assert.assertTrue;

/**
 * Created by liuqi on 2015-10-12.
 */
public class MySqlHandlerTest {
    private static String url_all_products = "http://192.168.1.71:8080/wala/test.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    private static String tmp = "";
    // products JSONArray
    //JSONArray product = null;


    static InputStream is = null;

    //JSONArray products = null;
    ArrayList<HashMap<String, String>> productsList;

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line = "a?";
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }


    @Before
    public void setUp() throws Exception {
        productsList = new ArrayList<HashMap<String, String>>();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testMySqlHandler() throws Exception {
        assertTrue(isConnected(4));

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        //JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
        //System.out.print("All Products: " + json.toString());

        //Httpclient
        //DefaultHttpClient httpClient = new DefaultHttpClient();
        //HttpGet httpGet = new HttpGet(url_all_products);
        //http response
        //HttpResponse response = httpClient.execute(httpGet);

        System.out.print("wala");

        URL url = new URL("http://192.168.1.71:8080/wala/get_all_products.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            if (urlConnection.getResponseCode() ==urlConnection.HTTP_OK)
            {
                Map<String, List<String>> map = urlConnection.getHeaderFields();
                System.out.println("Printing Response Header...\n");

                for (Map.Entry<String, List<String>> entry : map.entrySet())
                {
                    System.out.println("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
                }

                System.out.println(urlConnection.getResponseMessage());

                DataInputStream dis =new DataInputStream(urlConnection.getInputStream());
                StringBuffer inputLine = new StringBuffer();

                while ((tmp = dis.readLine()) != null) {
                    inputLine.append(tmp);
                    System.out.println(tmp);
                }

                String rs = inputLine.toString();
                //return new DataInputStream(urlConnection.getInputStream())
                Gson gson = new Gson();
                String json = gson.toJson(rs);
                System.out.println("temp"+ json);

                dis.close();
            }
        }finally {
            urlConnection.disconnect();
        }

    }
}

