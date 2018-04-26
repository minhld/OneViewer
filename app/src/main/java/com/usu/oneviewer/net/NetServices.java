package com.usu.oneviewer.net;

import com.usu.servicemiddleware.annotations.CommModel;
import com.usu.servicemiddleware.annotations.MobileService;
import com.usu.servicemiddleware.annotations.ServiceMethod;
import com.usu.servicemiddleware.annotations.SyncMode;
import com.usu.servicemiddleware.annotations.TransmitType;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@MobileService(
        commModel = CommModel.ClientServer,
        transmitType = TransmitType.Binary)
public class NetServices {

//    @ServiceMethod(syncMode = SyncMode.Async)
//    public InputStream getUrl(String url) {
//
//    }

//    @ServiceMethod(
//            syncMode = SyncMode.Async)
//    public byte[][] getUrl(String url) {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//
//                }
//            }
//        });
//
//        return new byte[0][0];
//    }
}
