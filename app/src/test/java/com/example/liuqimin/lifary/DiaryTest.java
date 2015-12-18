package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-11-01.
 * Unit test for Diary object for upload
 */

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DiaryTest {

    private static String tmp = "";

    @Test
    public void main() throws IOException, JSONException {

        Diary diary = new Diary(13);


        File f = new File("fonts/w1.jpg");
        if (f.exists()) {
            System.out.println("Drawable exist!");
            Drawable d = Drawable.createFromPath(String.valueOf(f));
        }
        else
            System.out.println("Drawable not found!");

        /* URL imageL
         * sun.awt.image.URLImageSource cannot be cast to java.io.InputStream
        String url = "http://localhost:8080/wala/image/124.jpg";
        Bitmap bitmap = null;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            d.draw(canvas);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        */

        URL url = new URL( "http://192.168.1.71:8080/wala/image/124.jpg");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            if (urlConnection.getResponseCode() ==urlConnection.HTTP_OK)
                System.out.println(urlConnection.getResponseMessage());

            DataInputStream dis =new DataInputStream(urlConnection.getInputStream());
            StringBuffer inputLine = new StringBuffer();

            while ((tmp = dis.readLine()) != null) {
                inputLine.append(tmp);
                //System.out.println(tmp);
            }
            String rs = inputLine.toString();
            //System.out.print(rs);
            diary.setImage(rs);

        } catch (IOException e) {
            e.printStackTrace();
        }

        diary.setId(44);
        diary.setSound("sound");
        diary.setDate("2011111");
        diary.setImage("asdfasdfjasdjkksakdkfkdkakdsffffasdf$%#^$%^#$%^ #$%^$%#^$%^ADFASDFASDFASDF");
        diary.addLocation(13, 123);

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Diary.class, new DiaryDeserializer());
        gsonBuilder.registerTypeAdapter(Diaries.class, new DiariesDeserializer());
        final Gson gson = gsonBuilder.create();

        String temp_p = gson.toJson(diary);

        //POST new obj to db

        URL url2 = new URL( "http://localhost:8080/wala/create_diary.php");
        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        //Send request
        DataOutputStream wr = new DataOutputStream(
                connection.getOutputStream ());
        wr.writeBytes(temp_p);


        System.out.println("wr:" + wr.size());
        //wr.writeBytes(URLEncoder.encode(jsonParam, "UTF-8"));
        wr.flush();
        wr.close();

        System.out.println("diary:" + temp_p);
        System.out.println("request sent");

        //Get Response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println("respons:" + response.toString());

        /*Strange example, null response
        * seems like we need extends AsyncTstk<>
        //String url2 = "http://localhost:8080/wala/create_diary.php";
        //DefaultHttpClient httpClient = new DefaultHttpClient();
        //HttpPost httpPost = new HttpPost(url2);
        //httpPost.setEntity(se);
        //httpPost.setHeader("Accept", "application/json");
        //httpPost.setHeader("Content-type", "application/json");
        */

    }

    public void DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
        try {
            File file;
            URL url = new URL("http://www.planwallpaper.com/static/images/Winter-Tiger-Wild-Cat-Images.jpg");
                    file = new File(fileName);

            long startTime = System.currentTimeMillis();
                        /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

                        /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("ImageManager", "download ready in"
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }

    }

}
