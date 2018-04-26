package com.usu.oneviewer.net;

import android.content.Context;

public class XWebServerHelper {
    private static XWebServer xServer;
    private static String innerHostName;

    public static void startServer(Context context) {
        try {
            xServer = new XWebServer(context, XWebServer.SERVER_DEF_HOST, XWebServer.SERVER_DEF_PORT);
            xServer.start();
        } catch (Exception e) {
            // if port is occupied by other third parties
            // port will be updated to a random number around the default value
            int newPort = XWebServer.SERVER_DEF_PORT + (int) (Math.random() * 200 + 1);
            try {
                xServer = new XWebServer(context, XWebServer.SERVER_DEF_HOST, newPort);
                xServer.start();
            } catch (Exception ex) {
                // no further catch, let it go
            }
        }
    }

    public static void stopServer() {
        if (xServer != null) {
            xServer.stop();
        }
    }

    /**
     * return the URL that contents are decrypted
     *
     * @param remoteUrl
     */
    public static String openUrl(String remoteUrl) {
        return "http://" + XWebServer.SERVER_DEF_HOST + ":" + XWebServer.SERVER_DEF_PORT +
                (remoteUrl.startsWith("/") ? "" : "/") + remoteUrl;
    }

}
