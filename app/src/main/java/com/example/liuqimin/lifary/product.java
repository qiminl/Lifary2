package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-24.
 */
public class Product {
    private String pid;
    private String name;
    private String price;
    private String created_at;
    private String updated_at;

    public Product(){
        pid = "";
        name = "";
        price = "";
        created_at = "";
        updated_at = "";
    }
    @Override
    public String toString() {
        return pid + " - " + name + " - " + price+ " - " + created_at+ " - " + updated_at;
    }

    public void setPid(String pid){if(notNull(pid))this.pid = pid;}
    public void setName(String name){if(notNull(name))this.name = name;}
    public void setPrice(String price){if(notNull(price))this.price = price;}
    public void setCreated_at(String created_at){if(notNull(created_at))this.created_at = created_at;}
    public void setUpdated_at(String updated_at){if(notNull(updated_at))this.updated_at = updated_at;}
    private boolean notNull (String test){
        if(test.isEmpty())
            return false;
        System.out.print("null update");
        return true;
    }
}
