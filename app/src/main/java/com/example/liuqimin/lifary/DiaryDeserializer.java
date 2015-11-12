package com.example.liuqimin.lifary;

/**
 * Created by liuqi_000 on 2015-10-28.
 * This class create the deserializer
 * of Diary.class
 */


import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiaryDeserializer implements JsonDeserializer<Diary> {

    @Override
    public Diary deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
    throws JsonParseException{
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonId = jsonObject.get("id");
        final int id = jsonId.getAsInt();
        final String date = jsonObject.get("date").getAsString();
        final String share = jsonObject.get("share").getAsString();
        final String image = jsonObject.get("image").getAsString();
        final String audio = jsonObject.get("audio").getAsString();

        final JsonArray jsonAuthorsArray = jsonObject.get("authors").getAsJsonArray();
        final String[] authors = new String[jsonAuthorsArray.size()];
        for (int i = 0; i < authors.length; i++) {
            final JsonElement jsonAuthor = jsonAuthorsArray.get(i);
            authors[i] = jsonAuthor.getAsString();
        }

        final Diary d= new Diary(id);
        d.setId(id);
        d.setImage(image);
        d.setAudio(audio);
        d.setShare(Integer.parseInt(share));
        d.setDate(date);

        return d;
    }

}
