package com.example.liuqimin.lifary;

/**
 * Created by liuqi_000 on 2015-10-28.
 * This class create the deserializer
 * of Diary.class
 */


import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DiaryDeserializer implements JsonDeserializer<Diary> {

    @Override
    public Diary deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
    throws JsonParseException{
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonId = jsonObject.get("id");
        final String id = jsonId.getAsString();
        final String date = jsonObject.get("date").getAsString();
        final String text = jsonObject.get("text").getAsString();
        final float latitude = jsonObject.get("latitude").getAsFloat();
        final float longitude = jsonObject.get("longitude").getAsFloat();
        final String share = jsonObject.get("share").getAsString();
        final String image = jsonObject.get("image").getAsString();
        final String imageurl = jsonObject.get("imageurl").getAsString();
        final String audio = jsonObject.get("sound").getAsString();
        final String userid = jsonObject.get("userid").getAsString();
        //Log.d("http","getAsString() till imageUri");
        final String imageUri = jsonObject.get("imageUri").getAsString();
        //Log.d("http","getAsString() finished upon imageUri");
        /*
        final JsonArray jsonAuthorsArray = jsonObject.get("authors").getAsJsonArray();
        final String[] authors = new String[jsonAuthorsArray.size()];
        for (int i = 0; i < authors.length; i++) {
            final JsonElement jsonAuthor = jsonAuthorsArray.get(i);
            authors[i] = jsonAuthor.getAsString();
        }*/

        final Diary d= new Diary(id);
        d.setId(id);
        d.setImage(image);
        d.setSound(audio);
        d.addContents(text);
        d.addLocation(latitude, longitude);
        d.setShare(Integer.parseInt(share));
        d.setDate(date);
        d.setImageUrl(imageurl);
        d.setUserid(userid);
        d.setImageUri(imageUri);

        Log.d("http", "diary d deserialize finished");
        return d;
    }

}
