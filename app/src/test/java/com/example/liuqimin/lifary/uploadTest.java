package com.example.liuqimin.lifary;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;


/**
 * Created by liuqi on 2015-11-09.
 */
public class uploadTest  {

    private String upload_file = "C:\\Users\\liuqi\\Desktop\\2.png";
    private static String  DEBUG_HTTP = "http";

    @Test
    public void main() throws IOException {
        //uploadTest();

        //registTest();

        loginTest();
        //downloadTest();

        //uploadFileTest();

        //downloadDiaryTest();

        //downloadSingleDiaryTest();
    }

    public void downloadSingleDiaryTest()throws IOException{
        String user_id = "1233333";
        ConnectionWithPost con = new ConnectionWithPost();
        Diaries diaries = con.getUserDiaries(user_id);
        System.out.println(diaries.getResponds());
        Diary[] diaryList = diaries.loadDiaries();
        for(Diary result : diaryList){
            result.print();
        }
    }

    public void downloadDiaryTest () throws IOException {

        ConnectionWithPost con = new ConnectionWithPost();
        Diaries diaries = con.getAllDiaries();
        Diary[] diaryList = diaries.loadDiaries();
        System.out.println(diaries.getResponds());
        for(Diary result : diaryList){
            result.print();
        }
    }

    public void uploadFileTest(){
        ConnectionWithPost con = new ConnectionWithPost();
        String image_url = con.uploadFile(upload_file);
        System.out.println("respond:" + image_url);
    }

    public void uploadTest() throws MalformedURLException {
        ConnectionWithPost con = new ConnectionWithPost();
        Diary diary = new Diary(6313);
        diary.setId(5);
        diary.setSound("sound");
        diary.setDate();
        diary.setImage("awwww");
        diary.setImageUrl(con.uploadFile(upload_file));
        diary.addLocation(13, 123);
        diary.setUserid("12333333");

        HashMap<String, String> map = diary.toHashMap();

        String url = "http://192.168.1.71:8080/wala/upload_diary.php";
        String respond = con.uploadDiary(map, url);
        System.out.println("respond:" + respond);
    }
    public void downloadTest() throws IOException {
        String file_name = "download_file.jpg";
        String file_url = "http://192.168.1.71:8080/wala/image/1.jpg";
        DownloadFromServer dl = new DownloadFromServer();
        String file_location =dl.DownloadFromServer(file_url, file_name);
        System.out.println("downloaded file location:" + file_location);
    }

    public void loginTest() throws MalformedURLException {
        ConnectionWithPost con = new ConnectionWithPost();
        String response;
        HashMap<String, String> map = new HashMap<>();
        map.put("email","gsngsngsn@gmail.com    ");
        map.put("password","123");
        String url = "http://192.168.1.71:8080/wala/login.php";
        response = con.uploadDiary(map, url);
        System.out.println("respond:" + response);
    }

    public void registTest() throws MalformedURLException {
        ConnectionWithPost con = new ConnectionWithPost();
        String response;
        HashMap<String, String> map = new HashMap<>();
        map.put("email","gsngsngsn@gmail.com");
        map.put("password","123");
        map.put("name","hoho");
        String url = "http://192.168.1.71:8080/wala/register.php";
        response = con.uploadDiary(map, url);
        System.out.println("respond:" + response);
    }


}
