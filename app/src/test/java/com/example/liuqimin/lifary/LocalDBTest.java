package com.example.liuqimin.lifary;

import android.test.InstrumentationTestCase;

/**
 * Created by liuqi on 2015-11-17.
 * Test Contex
 */

//once extend ActivityTestCase, methods begins with test will be executed
public class LocalDBTest extends InstrumentationTestCase {

    private String upload_file = "C:\\Users\\liuqi\\Desktop\\2.png";
    private DiaryDBHandler db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.out.println("test entry context " + getInstrumentation().getTargetContext().toString());

    }

    @Override
    public void tearDown() throws Exception {
//        db.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        System.out.println("test entry context " + getInstrumentation().getTargetContext().toString());
        ConnectionWithPost con = new ConnectionWithPost();
        Diary diary = new Diary(6313);
        diary.setId(5);
        diary.setSound("sound");
        diary.setDate();
        diary.setImage("awwww");
        diary.setImageUrl(con.uploadFile(upload_file));
        diary.addLocation(13, 123);
        diary.setUserid("1233312");
        db = new DiaryDBHandler(getInstrumentation().getTargetContext());
        db.addDiary(diary);
        // Here i have my new database wich is not connected to the standard database of the App

        Diary result = db.findDiaryByID(5);
        result.print();
    }
}
