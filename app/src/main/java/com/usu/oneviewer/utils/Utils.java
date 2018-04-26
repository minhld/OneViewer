package com.usu.oneviewer.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

    public static void toast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static void printLog(Activity c, final TextView log, final String msg) {
        c.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // log.append(msg + "\r\n");
                // log.setText(msg + "\r\n" + log.getText());
                log.setText(msg + log.getText());
            }
        });
    }

    public static void printLog(Activity c, final TextView log, final Object msg) {
        c.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // log.append((SpannableStringBuilder) msg);
                // log.setText(msg + "\r\n" + log.getText());
                log.setText(msg + "" + log.getText());
            }
        });
    }

    /**
     * convert a binary array to an input stream
     *
     * @param data
     * @return
     */
    public static InputStream getStream(byte[] data) {
        // avoid null exception
        if (data == null) data = new byte[0];

        return new ByteArrayInputStream(data);
    }
}
