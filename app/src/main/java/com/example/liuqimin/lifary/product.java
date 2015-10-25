package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-24.
 */
public class product {
    private String pid;
    private String name;
    private String price;
    private String created_at;
    private String updated_at;


    @Override
    public String toString() {
        return pid + " - " + name + " - " + price+ " - " + created_at+ " - " + updated_at;
    }
}
