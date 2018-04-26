package com.usu.oneviewer.net;

import com.usu.tinyservice.messages.binary.ResponseMessage;
import com.usu.tinyservice.network.Broker;
import com.usu.tinyservice.network.Client;
import com.usu.tinyservice.network.NetUtils;
import com.usu.tinyservice.network.ReceiveListener;
import com.usu.tinyservice.network.Worker;

import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    public static Broker broker;
    public static List<NetworkServiceWorker> workers = new ArrayList<>();
    public static NetworkServiceClient client;
    public static ClientHandler handler;

    public static String wfdBrokerIp = "";
    public static String wifiBrokerIp = "";

    public static void initBroker(String brokerIp) {
        broker = new Broker(brokerIp);
    }

    public static void initWorker(String brokerIp) {
        NetworkServiceWorker worker = new NetworkServiceWorker(brokerIp);
        workers.add(worker);
    }

    public static NetworkServiceClient initClient(String brokerIp) {
        client = new NetworkServiceClient(wfdBrokerIp, new ReceiveListener() {
            @Override
            public void dataReceived(String idChain, String funcName, byte[] data) {
                ResponseMessage resp = (ResponseMessage) NetUtils.deserialize(data);
                if (resp.functionName.equals(NetUtils.BROKER_INFO)) {
                    // a denied message from the Broker
                    String msg = (String) resp.outParam.values[0];
                    // UITools.printLog(MainActivity.this, infoText, "[Client-" + client.client.clientId + "] Error " + msg);
                } else if (resp.functionName.equals("getUrl")) {
                    byte[] msg = (byte[]) resp.outParam.values[0];
                    if (handler != null) handler.responseReceived(msg);
                    // UITools.printLog(MainActivity.this, infoText, "[Client-" + client.client.clientId + "] Received: " + msgs[0]);
                }
            }
        });
        return client;
    }

    /**
     * destroy all brokers, clients and workers available on this device
     */
    public static void disconnectAll() {
        if (client != null) {
            client.close();
            client = null;
        }
        if (broker != null) {
            broker.close();
            broker = null;
        }
        for (NetworkServiceWorker worker : workers) {
            worker.close();
        }
    }

    public static void setClientHandler(ClientHandler handler) {
        NetworkUtils.handler = handler;
    }

    public interface ClientHandler {
        void responseReceived(byte[] resp);
    }
}
