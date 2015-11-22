package com.example.liuqimin.lifary;

import android.content.res.Resources;
import android.test.ActivityTestCase;

import com.example.liuqimin.lifary.ConnectionWithPost;
import com.example.liuqimin.lifary.Diary;
import com.example.liuqimin.lifary.DiaryDBHandler;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.Test;


/**
 * Created by liuqi on 2015-11-17.
 * Test Contex
 */

//once extend ActivityTestCase, methods begins with test will be executed
public class LocalDBTest extends AndroidTestCase{

    @Test
    public void main() {
        assertSame(2,2);
    }

    @Test
    public void testId(){

        assertSame(1,2);
    }

}
