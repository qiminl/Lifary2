package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-05.
 */
public class AppConfig {
    // server user login url
    public static String URL_LOGIN = "http://192.168.1.71:8000/android_api/login.php";
    //register url
    public static String URL_REGISTER = "http://192.168.1.71:8000/android_api/register.php";

    public static boolean dude(int num){
        if (num <10)
            return true;
        else
            return false;
    }
}
