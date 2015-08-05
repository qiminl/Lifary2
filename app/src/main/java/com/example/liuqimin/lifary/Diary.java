package com.example.liuqimin.lifary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by liuqimin on 15-07-07.
 */
public class Diary {

    private int id;
    private String date;
    private String text;
    private float latitude, longitude;
    private int share;
  //  private byte[]img;
   // private byte[] sound;

    private String image;
    private String sound;

    public Diary(int a){
        Calendar c= Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date = month + "-" + day + "-" + year + "  " +
                hour + ":" + minute + ":" + seconds;


     //   img = null;
        image = "";
        sound = "";
        text = "";
        latitude = 0;
        longitude = 0;
        share = 0;

    }
    public void addContents(String contents){
        text = contents;
    }

    public void addLocation(float lat, float lon){
        latitude = lat;
        longitude = lon;
    }
    public void addImage(Bitmap bmp){

        byte[] img = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        img = os.toByteArray();
        if(img == null)
            image = "";
        else    image = Base64.encodeToString(img, Base64.DEFAULT);


        Log.d("Lifary", "Diary: img == " + Arrays.toString(img));

    }
    public void addSound(byte[] audioByte){
        if(audioByte != null) {
            sound = Base64.encodeToString(audioByte, Base64.DEFAULT);
        }
      //  Log.d("Lifary", "sound.size = " + sound.length);
    }
    public void setImageByByte(byte[] imgByte){
        if(imgByte != null)
           image = Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
    public void setAudioByte(byte[] audioByte){
        if(audioByte != null)
            sound = Base64.encodeToString(audioByte, Base64.DEFAULT);
    }

    public void setShare(int s){
        share = s;
    }
    public void setDate(String d){date = d;}

    public void setId(int i){this.id = i;}
    public int getId(){ return id;}
    public String getDate(){return date;}
    public String getContent(){return text;}
    public float getLatitude(){return latitude;}
    public float getLongitude(){return longitude;}
    public byte[] getImg(){
            byte[] img = null;
            img = Base64.decode(image,Base64.DEFAULT);
        return img;
    }
    public byte[] getAudio(){
        byte[] soundByte = null;
        soundByte = Base64.decode(sound,Base64.DEFAULT);
        return soundByte;
    }
    public int getShare(){return share;}
    public Bitmap getImgBitmap(){

        byte[] img = getImg();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Log.d("Lifary", "Diary: img.size === " + img.length);

        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        Log.d("Lifary", "Diary: bitmap.size === " + bitmap.getByteCount());

        return bitmap;
    }

    public String getImage(){return image;}
    public String getSound(){return  sound;}

    public void print() {
        Log.d("fb", "pringting diar:");
        Log.d("fb", "image: " + image);
        Log.d("fb", "id" + id);
        Log.d("fb", "date" + date);
        Log.d("fb", "text" + text);
        Log.d("fb", "lat" + latitude + " long:" + longitude);
        Log.d("fb", "share" + share);
        //private byte[]img;
        //private byte[] sound;
        Log.d("fb", "sound" + sound);
    }

    public String getText(){
        return text;
    }

}
