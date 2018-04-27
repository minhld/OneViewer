package com.usu.oneviewer.net;

import android.content.Context;
import android.util.Log;

import com.usu.oneviewer.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class XWebServer extends NanoHTTPD {
    public static final String SERVER_DEF_HOST = "localhost";
    public static final int SERVER_DEF_PORT = 3883;

    final String TAG = "XWebServer";

    String mime;
    Response resp;

    static final String MIME_DEFAULT_BINARY = "application/octet-stream";
    static final Map<String,String> mimeTypes = new HashMap<String, String>() {{
        put("css", "text/css");
        put("htm", "text/html");
        put("html", "text/html");
        put("xhtml", "application/xhtml+xml");
        put("xml", "text/xml");
        put("json", "application/json");
        put("java", "text/x-java-source, text/java");
        put("md", "text/plain");
        put("txt", "text/plain");
        put("asc", "text/plain");
        put("gif", "image/gif");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
        put("mp3", "audio/mpeg");
        put("m3u", "audio/mpeg-url");
        put("mp4", "video/mp4");
        put("ogv", "video/ogg");
        put("flv", "video/x-flv");
        put("mov", "video/quicktime");
        put("swf", "application/x-shockwave-flash");
        put("js", "application/javascript");
        put("pdf", "application/pdf");
        put("doc", "application/msword");
        put("ogg", "application/x-ogg");
        put("zip", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
    }};

    Context context;

    public XWebServer(Context context) {
        super(XWebServer.SERVER_DEF_HOST, XWebServer.SERVER_DEF_PORT);
        this.context = context;
    }

    public XWebServer(Context context, String hostName, int port){
        super(hostName, port);
        this.context = context;
        setClientHandler();
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if (uri.startsWith("/")) uri = uri.substring(1);

        // deny some strange URIs
        if (uri.contains("favicon") || !uri.startsWith("http")){
            return null;
        }

        // Get MIME type from file name extension, if possible
        mime = MIME_DEFAULT_BINARY;
        int dot = uri.lastIndexOf('.');
        if (dot >= 0) {
            mime = mimeTypes.get(uri.substring(dot + 1).toLowerCase());
        }

        // reset the response
        synchronized (NetworkUtils.client) {
            resp = null;

            NetworkUtils.client.getUrl(uri);
            // NetworkUtils.initClient("").getUrl(uri);

            // wait
            while (resp == null) {
                Utils.sleep(100);
            }
            return resp;
        }
    }

    private void setClientHandler() {
        NetworkUtils.setClientHandler(new NetworkUtils.ClientHandler() {
            @Override
            public void responseReceived(byte[] data) {
                try {
                    InputStream is = Utils.getStream(data);
                    resp = new Response(Response.Status.OK, mime, is, is.available());
                    if (resp != null) {
                        String eTag = Integer.toHexString(new Random().nextInt());
                        resp.addHeader("ETag", eTag);
                        resp.addHeader("Connection", "Keep-alive");
                    }
                } catch (IOException e) {
                    InputStream is = Utils.getStream(new byte[0]);
                    resp = new Response(Response.Status.OK, mime, is, 0);
                }
            }
        });
    }
}
