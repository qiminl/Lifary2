package com.example.liuqimin.lifary;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by liuqi on 2015-10-31.
 * GSon component:
 *      Deserializer for  multiple diaries: convert them into an array of Diary objs.
 */
public class DiariesDeserializer  implements JsonDeserializer<Diaries> {

    @Override
    public Diaries deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        Diary[] diary = context.deserialize(jsonObject.get("diaries"), Diary[].class);
        final String success = jsonObject.get("success").getAsString();
        final String message = jsonObject.get("message").getAsString();

        final Diaries diaries = new Diaries();
        diaries.setProducts(diary);
        diaries.setResponds(success, message);
        return diaries;
    }
}
