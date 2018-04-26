package com.usu.oneviewer.net;

import com.usu.tinyservice.network.Broker;
import com.usu.tinyservice.network.Client;
import com.usu.tinyservice.network.Worker;

import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    public static Broker broker;
    public static List<Worker> workers = new ArrayList<>();
    public static Client client;

    public static String wfdBrokerIp = "";
    public static String wifiBrokerIp = "";

    public static void initBroker(String brokerIp) {
        broker = new Broker(brokerIp);
    }

    public static void initWorker(String brokerIp) {
        Worker worker = new Worker(brokerIp) {
            @Override
            public String info() {
                return null;
            }
        };
        workers.add(worker);
    }

    public static void initClient() {

    }
}
