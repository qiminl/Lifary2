package com.example.liuqimin.lifary;

/**
 * Created by liuqimin on 15-07-04.
 * todo encrypt pw
 * todo get the stupid unique id auto set? or retrieve from server?
 */
public class User {

    private int _id;
    private String _unique = null;
    private String _username;
    private String _password;
    private String updateNumbers; // use to identify if need to update table.

    public User(){}

    public User(String username, String password){
        _username = username;
        _password = password;

    }
    public User(int id, String username, String password){
        _id = id;
        _username = username;
        _password = password;
    }

    public void setID(int id){ _id = id;}
    public int getID(){ return _id; }
    public void setUsername(String username){
        _username = username;
    }
    public String getUsername(){
        return _username;
    }
    public void setPassword(String password){
        _password = password;
    }
    public String getPassword(){
        return _password;
    }
    public void set_unique(String unique){_unique = unique;}
    public String get_unique(){return _unique;}
    public void setUpdateNumbers(String unique){updateNumbers = unique;}
    public String getUpdateNumbers(){return updateNumbers;}

}
