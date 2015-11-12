package com.example.liuqimin.lifary;

/**
 * Created by liuqi on 2015-10-31.
 */
public class Diaries {
    private Diary[] diaries;

    public void setProducts (Diary[] diaries){this.diaries = diaries;}
    @Override
    public String toString (){
        String results = "";
        for (Diary diary:diaries) {
            results = results+"\n"+diary.toString();
        }
        return results;
    }
}
