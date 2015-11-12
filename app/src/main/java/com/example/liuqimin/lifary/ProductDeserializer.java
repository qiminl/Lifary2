package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-29.
 */

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ProductDeserializer implements JsonDeserializer<Product> {
    @Override
    public Product deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException{

        final JsonObject jsonObject = json.getAsJsonObject();
        final String pid = jsonObject.get("pid").getAsString();
        final String name = jsonObject.get("name").getAsString();
        final String price = jsonObject.get("price").getAsString();
        final String created_at = jsonObject.get("created_at").getAsString();
        final String updated_at = jsonObject.get("updated_at").getAsString();

        final Product d= new Product();
        d.setPid(pid);
        d.setName(name);
        d.setPrice(price);
        d.setCreated_at(created_at);
        d.setUpdated_at(updated_at);

        return d;
    }
}
