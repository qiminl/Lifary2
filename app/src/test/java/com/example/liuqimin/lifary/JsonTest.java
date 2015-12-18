package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-23.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
public class JsonTest {

    //string that read in server response;
    private static String tmp = "";

    @Test
    public void main() throws IOException {
        //System.out.println("Hello, World!");
        JsonTest();
    }

    public void JsonTest() throws IOException {

        //testing gson
        /*
        String jsonStr = "{\"phonetype\":\"N95\",\"cat\":\"WP\"}";
        Gson gson = new Gson();
        String json = gson.toJson(jsonStr);
        System.out.println(json);
        */
        //URL url = new URL("http://192.168.1.71:8080/wala/get_all_products.php");
        URL url = new URL("http://192.168.1.71:8080/wala/get_all_diaries.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            if (urlConnection.getResponseCode() ==urlConnection.HTTP_OK)
                System.out.println(urlConnection.getResponseMessage());

            DataInputStream dis =new DataInputStream(urlConnection.getInputStream());
            StringBuffer inputLine = new StringBuffer();

            while ((tmp = dis.readLine()) != null) {
                inputLine.append(tmp);
                System.out.println(tmp);
            }
            String rs = inputLine.toString();

            /*
             *Using Gson builder with deserializer
             */
            final GsonBuilder gsonBuilder = new GsonBuilder();
            //gsonBuilder.registerTypeAdapter(Product.class, new ProductDeserializer());
            //gsonBuilder.registerTypeAdapter(Products.class, new ProductsDeserializer());

            gsonBuilder.registerTypeAdapter(Diary.class, new DiaryDeserializer());
            gsonBuilder.registerTypeAdapter(Diaries.class, new DiariesDeserializer());
            final Gson gson = gsonBuilder.create();
            //final Products products = gson.fromJson(rs, Products.class);
            final Diaries diaries = gson.fromJson(rs, Diaries.class);
            //System.out.println("Convert json string into object: \n" + diaries);
            String temp_p = gson.toJson(diaries);
            //System.out.println("Convert object into json format: \n" + temp_p);
            Diary [] diaryList = diaries.loadDiaries();
            for(Diary result : diaryList){
                result.print();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
