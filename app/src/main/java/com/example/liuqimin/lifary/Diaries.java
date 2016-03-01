package com.example.liuqimin.lifary;

import java.util.List;

/**
 * Object Class contains Diary array and server responds structure
 * Created by liuqi on 2015-10-31.
 */
public class Diaries {
    //@todo refactor it as List?
    private Diary[] diaries;
    private String success;
    private String message;

    public Diary[] loadDiaries(){ return this.diaries;}

    public Diary loadDiaries(int id){
        for (Diary result : diaries){
            if (result.getId() == id){
                return result;
            }
        }
        return null;
    }

   // public void addDairy(Diary diary){this.diaries.}

    public void setProducts (Diary[] diaries){this.diaries = diaries;}
    public void setDiaries (List<Diary> list){diaries =  list.toArray(new Diary[list.size()]);}
    public void setResponds (String success, String message){this.message = message; this.success = success;}

    public String getResponds(){return "success: " + this.success + "\nmeesge: " +this.message;}
    @Override
    public String toString (){
        String results = "";
        for (Diary diary:diaries) {
            results = results+"\n"+diary.toString();
        }
        return results;
    }
}
