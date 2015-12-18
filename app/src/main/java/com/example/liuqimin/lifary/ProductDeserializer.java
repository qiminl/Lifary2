package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-29.
 */

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ProductDeserializer implements JsonDeserializer<Product> {
    @Override
    public Product deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException{


        final JsonObject jsonObject = json.getAsJsonObject();
        final String pid = jsonObject.get("pid").getAsString();
        final String name = jsonObject.get("name").getAsString();
        final String price = jsonObject.get("price").getAsString();

        //eliminate null exception
        String updated_at = "null";
        String created_at = "null";
        final JsonElement created_at_Json = jsonObject.get("created_at");
        final JsonElement update_at_Json = jsonObject.get("updated_at");
        if(!(update_at_Json.isJsonNull())) {
            updated_at = update_at_Json.getAsString();
        }
        if(!(created_at_Json.isJsonNull())){
            created_at =  created_at_Json.getAsString();
        }

        final Product d= new Product();
        d.setPid(pid);
        d.setName(name);
        d.setPrice(price);
        d.setCreated_at(created_at);
        d.setUpdated_at(updated_at);

        return d;
    }
}
