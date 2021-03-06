package com.example.liuqimin.lifary;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by liuqimin on 15-07-07.
 */
public class DiaryHelper {

    private double id;
    private String date;
    private String text;
    private float latitude, longitude;
    private int share;
    //private byte[]img;
    private String image;
    //private byte[] sound;
    private String sound;

    public  DiaryHelper(){}

    public  DiaryHelper(int a){
        Calendar c= Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date = month + "-" + day + "-" + year + "  " +
                hour + ":" + minute + ":" + seconds;

        //Log.d("Lifary", "date ===== " + date);

        id  = a;
        //img = null;
        image = "image";
        //sound = null;
        sound = "sound";
        text = "text";
        latitude = 0;
        longitude = 0;
        share = 0;
    }
    public void addImage(Bitmap bmp){
        byte [] img = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        img = os.toByteArray();
        //Log.d("fb", "copy byte b to img , img.size = " + img.length);
        image = Base64.encodeToString(img, Base64.DEFAULT);
        if (image == null)
            image = "";
        //Log.d("fb", "image: " + image);
    }

    public void addSound(byte[] audioByte){
        try {
            byte[] sound_temp = null;
            sound_temp = audioByte;
            //Log.d("fb", "sound.size = " + sound_temp.length);
            sound = Base64.encodeToString(sound_temp, Base64.DEFAULT);
        }
        catch (Exception e){
            Log.d("fb", "sound wrong");
        }
        //Log.d("fb", "sound: " + sound);
    }

    public void addLocation(float lat, float lon){
        latitude = lat;
        longitude = lon;
        //Log.d("fb", "lat: " + latitude +" long:" + longitude);
    }
    public void setShare(int s){
        share = s;
    }
    public void setDate(String d){
        date = d;
    }
    public void setId(double i){
        id = i;
    }

    public void convert(Diary d){
        id = d.getId();
        date = d.getDate();
        sound =d.getSound();
        text = d.getText();
        image = d.getImage();
        latitude = d.getLatitude();
        longitude = d.getLongitude();
        share = d.getShare();
    }
    public void convert(DiaryHelper d){
        id = d.getId();
        date = d.getDate();
        sound =d.getSound();
        text = d.getText();
        image = d.getImage();
        latitude = d.getLatitude();
        longitude = d.getLongitude();
        share = d.getShare();
    }

    public void past(DiaryHelper dd){
        dd.setId(id);
        dd.setDate(date);
        dd.setSound(sound);
        dd.setText(text);
        dd.setImage(image);
        dd.setLongitude(longitude);
        dd.setLatitude(latitude);
        dd.setShare(share);
        Log.d("Lifary", "diaryhelper past done");
    }


    private void setLatitude(float v) {
        latitude = v;
    }

    private void setLongitude(float v) {
        longitude = v;
    }

    private void setImage(String s) {
        image = s;
    }

    private void setText(String text) {
        this.text = text;
    }

    private void setSound(String s) {
        //Log.d("fb","sound");
        sound = s;
        //Log.d("fb","sound done");
    }

    public double getId(){ return id;}
    public String getDate(){return date;}
    public String getSound(){return sound;}
    public String getText(){return text;}
    public String getImage(){return image;}
    public float getLatitude(){return latitude;}
    public float getLongitude(){return longitude;}
    public int getShare(){return share;}
    public void print(){
        Log.d("fb","pringting diar:");
        Log.d("fb", "image: " + image);
        Log.d("fb", "id" +id);
        Log.d("fb", "date" +date);
        Log.d("fb", "text" +text);
        Log.d("fb", "lat" +latitude +" long:" +longitude);
        Log.d("fb", "share" +share);
        //private byte[]img;
        //private byte[] sound;
        Log.d("fb", "sound" +sound);
        Log.d("fb","pringting diar:");
        Log.d("fb","pringting diar:");
        Log.d("fb","pasdfasdfasdfasdfassdf :");

    }
}
