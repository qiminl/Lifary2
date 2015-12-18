package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-29.
 */
public class Products {
    private Product[] products;

    public void setProducts (Product[] products){this.products = products;}
    @Override
    public String toString (){
        String results = "";
        for (Product product: products) {
            results = results+"\n"+product.toString();
        }
        return results;
    }

}
