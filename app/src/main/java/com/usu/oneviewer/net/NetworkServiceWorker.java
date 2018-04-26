package com.usu.oneviewer.net;

import com.usu.tinyservice.messages.binary.RequestMessage;
import com.usu.tinyservice.messages.binary.ResponseMessage;
import com.usu.tinyservice.network.NetUtils;
import com.usu.tinyservice.network.Worker;

public class NetworkServiceWorker {
    NetworkService networkService;

    public NetworkServiceWorker() {
        this(NetUtils.DEFAULT_IP);
    }

    public NetworkServiceWorker(String brokerIp) {
        networkService = new NetworkService();
        new WorkerX(brokerIp);
    }

    class WorkerX extends Worker {
        public WorkerX() {
            super();
        }

        public WorkerX(String brokerIp) {
            super(brokerIp);
        }


        @Override
        public byte[] resolveRequest(byte[] packageBytes) {
            byte[] respBytes = null;

            // get request message
            RequestMessage reqMsg = (RequestMessage) NetUtils.deserialize(packageBytes);

            switch (reqMsg.functionName) {
                case "getUrl": {
                    // for variable "url"
                    java.lang.String[] urls = new java.lang.String[reqMsg.inParams[0].values.length];
                    for (int i = 0; i < reqMsg.inParams[0].values.length; i++) {
                        urls[i] = (java.lang.String) reqMsg.inParams[0].values[i];
                    }
                    java.lang.String url = urls[0];

                    // start calling function "getUrl"
                    byte[] rets = networkService.getUrl(url);
                    String retType = "byte[]";
                    ResponseMessage respMsg = new ResponseMessage(reqMsg.messageId, reqMsg.functionName, retType, new Object[] { rets });

                    // convert to binary array
                    respBytes = NetUtils.serialize(respMsg);
                    break;
                }
            }

            return respBytes;

        }

        @Override
        public String info() {
            String json =
                    "{" +
                    "\"code\" : \"REGISTER\"," +
                    "\"id\" : \"" + workerId + "\"," +
                    "\"functions\" : [" +
                    "{" +
                    "\"functionName\" : \"getUrl\"," +
                    "\"inParams\" : [\"java.lang.String\"]," +
                    "\"outParam\" : \"byte[]\"" +
                    "}" +
                    "]" +
                    "}";
            return json;
        }
    }
}
