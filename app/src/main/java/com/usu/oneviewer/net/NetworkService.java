package com.usu.oneviewer.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkService {
    static OkHttpClient client;

    public byte[] getUrl(String url) {
        if (client == null) {
            client = new OkHttpClient.Builder().build();
        }

        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("unexpected code " + response);
            }
            // InputStream is = response.body().byteStream();
            return response.body().bytes();
        } catch (IOException e) {
            return new byte[0]; // empty
        }
    }

//    public static InputStream getSecureUrl(String url) throws IOException {
//        OkHttpClient client = new OkHttpClient.Builder().build();
//        client = trustAllSslClient(client);
//        Request request = new Request.Builder().url(url).build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        return response.body().byteStream();
//    }
//
//    public static InputStream receive(String url) throws IOException {
//        if (url.startsWith("https")) {
//            return getSecureUrl(url);
//        } else {
//            return getUrl(url);
//        }
//    }
//
//    private static final TrustManager[] trustAllCerts = new TrustManager[] {
//        new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
//            }
//
//            @Override
//            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
//            }
//
//            @Override
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                return new java.security.cert.X509Certificate[]{};
//            }
//        }
//    };
//
//    private static final SSLContext trustAllSslContext;
//    static {
//        try {
//            trustAllSslContext = SSLContext.getInstance("SSL");
//            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();
//
//    public static OkHttpClient trustAllSslClient(OkHttpClient client) {
//        OkHttpClient.Builder builder = client.newBuilder();
//        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager)trustAllCerts[0]);
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
//        return builder.build();
//    }
}
