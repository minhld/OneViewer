package com.usu.oneviewer.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.usu.oneviewer.support.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Minh Le on 3/13/2018.
 */

public class Utils {
    public final static int DELAY_TIME = 1000;
    public final static int DELAY_BACK_PRESS = 3000;
    static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");

    public static User currentUser = new User();

    public static void displayRoundImageFromUrl(Context c, String url, ImageView view) {

    }

    public static String formatDateTime(long time) {
        return SDF.format(time);
    }

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
