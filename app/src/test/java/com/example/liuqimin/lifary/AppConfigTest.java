package com.example.liuqimin.lifary; /**
 * Created by liuqi on 2015-10-11.
 */

import com.example.liuqimin.lifary.AppConfig;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AppConfigTest {
    @Test
    public void main() {
        System.out.println("Hello, World!");
        AppTest();
    }

    public void AppTest() {

        assertTrue("wrong?",AppConfig.dude(40));
        System.out.println("errrr");
    }
}