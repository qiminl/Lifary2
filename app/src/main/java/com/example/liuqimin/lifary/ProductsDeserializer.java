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


public class ProductsDeserializer implements JsonDeserializer<Products>{
    @Override
    public Products deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        Product[] product = context.deserialize(jsonObject.get("products"), Product[].class);

        final Products products = new Products();
        products.setProducts(product);
        return products;
    }
}
