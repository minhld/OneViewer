package com.usu.utils;

/**
 * Created by Minh Le on 3/13/2018.
 */

public class Utils {
    public final static int DELAY_TIME = 1000;
    public final static int DELAY_BACK_PRESS = 3000;

    public static void sleep(int delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch(Exception e) {}
    }
}
