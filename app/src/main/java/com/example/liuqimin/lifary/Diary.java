package com.example.liuqimin.lifary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by liuqimin on 15-07-07.
 * Diary object class:
 *      unique id in string;
 *      data, text, image, sound all in string;
 *      location data in float.
 */
public class Diary {

    private String id;
    private String date;
    private String text;
    private float latitude, longitude;
    private int share;
  //  private byte[]img;
   // private byte[] sound;
    private String image;
    private String imageurl;
    private String imageUri;//local uri of image
    private String sound;
    private String userid;

    //todo modify init stat - create a method to full init with content
    public Diary(int a){
        setDate();
        image = sound = userid =""; imageurl = text = imageUri="-1";
        latitude = longitude = share = 0;
    }
    public Diary(String a){
        setDate(); this.id = a;
        image = sound = userid =""; imageurl = text = imageUri="-1";
        latitude = longitude = share = 0;
    }
    public void addContents(String contents){
        text = contents;
    }
    public void addLocation(float lat, float lon){
        latitude = lat;
        longitude = lon;
    }
    public void setImage(String image){
        this.image = image;
    }
    public void setSound(String sound){
        this.sound = sound;
    }
    public void addImage(Bitmap bmp){

        byte[] img;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        img = os.toByteArray();
        if(img == null)
            image = "";
        else    image = Base64.encodeToString(img, Base64.DEFAULT);
        //Log.d("Lifary", "Diary: img == " + Arrays.toString(img));
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
    public void setImageUrl(String url){
        imageurl = url;
    }
    public void setShare(int s){
        share = s;
    }
    public void setDate(String date){this.date = date;}

    /**
     * mark the date of the system when diary is created.
     */
    public void setDate(){
        Calendar c= Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date = month + "-" + day + "-" + year + "  " +
                hour + ":" + minute + ":" + seconds;
    }
    public void setId(double i){this.id = Double.toString(i);}
    public void setId(String i){this.id =i;}
    public void setUserid(String diaryid){this.userid = diaryid;}
    public double getId(){ return Double.parseDouble(this.id);}
    public String getDate(){return date;}
    public String getContent(){return text;}
    public float getLatitude(){return latitude;}
    public float getLongitude(){return longitude;}
    public int getShare(){return share;}
    public byte[] getImg(){
            byte[] img;
            img = Base64.decode(image,Base64.DEFAULT);
        return img;
    }
    public byte[] getAudio(){
        byte[] soundByte;
        soundByte = Base64.decode(sound,Base64.DEFAULT);
        return soundByte;
    }
    public Bitmap getImgBitmap(){
        byte[] img = getImg();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Log.d("Lifary", "Diary: img.size === " + img.length);

        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        Log.d("Lifary", "Diary: bitmap.size === " + bitmap.getByteCount());

        return bitmap;
    }
    public String getImageUrl(){ return imageurl;}
    public String getImage(){return image;}
    public String getSound(){return  sound;}
    public String getUserid(){return userid;}
    public void print() {
        //Log.d("fb", "pringting diar:");
        Log.d("fb", "image: " + image);
        Log.d("fb", "id" + id);
        Log.d("fb", "date" + date);
        Log.d("fb", "text" + text);
        Log.d("fb", "lat" + latitude + " long:" + longitude);
        Log.d("fb", "share" + share);
        //private byte[]img;
        //private byte[] sound;
        Log.d("fb", "sound" + sound);
        System.out.println("id:" + id);
        System.out.println("imageurl:"+ imageurl);
    }

    /**
     * Converting for FireBase Diary helper.
     * todo  to be deleted;
     */
    public void convert(DiaryHelper d){
        id = Double.toString(d.getId());
        date = d.getDate();
        sound =d.getSound();
        text = d.getText();
        image = d.getImage();
        latitude = d.getLatitude();
        longitude = d.getLongitude();
        share = d.getShare();
    }
    public boolean hasImage(){
        //simplified if.
        return this.image.length() >= 10;
    }
    public void setImageUri(String imagePath){
        imageUri = imagePath;
    }
    public String getImageUri(){
        return imageUri;
    }
    public HashMap<String, String> toHashMap(){
        HashMap<String, String> map = new HashMap<>();

        Log.d("fb","hashmap");
        //@todo more thoughts on diary id's uniqueness
        //map.put("id", Double.toString(this.id));
        map.put("id",this.id);
        map.put("date", this.date);
        map.put("text", this.text);
        map.put("latitude", Float.toString(this.latitude));
        map.put("longitude", Float.toString(this.longitude));
        map.put("share",Integer.toString(this.share));
        map.put("image", this.image);
        map.put("imageurl", this.imageurl);
        Log.d("http", "image url " + this.imageurl);
        map.put("sound", this.sound);
        map.put("userid", this.userid);
        map.put("imageUri",this.imageUri);

        Log.d("http", "imageUri " + map.get("imageUri"));
        Log.d("fb", "hash done");
        return map;
    }
    public String getText(){
        return text;
    }

}
