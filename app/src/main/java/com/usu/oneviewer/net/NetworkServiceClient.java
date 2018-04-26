package com.usu.oneviewer.net;

import com.usu.tinyservice.messages.binary.InParam;
import com.usu.tinyservice.messages.binary.RequestMessage;
import com.usu.tinyservice.network.Client;
import com.usu.tinyservice.network.NetUtils;
import com.usu.tinyservice.network.ReceiveListener;

public class NetworkServiceClient {
    ReceiveListener listener;
    RmiClient client;

    public NetworkServiceClient(ReceiveListener listener) {
        // start listener
        this.listener = listener;

        // create request message and send
        client = new RmiClient();
    }

    public NetworkServiceClient(String brokerIp, ReceiveListener listener) {    // start listener
        this.listener = listener;

        // create request message and send
        client = new RmiClient(brokerIp);
    }

    public void getUrl(java.lang.String url) {
        // compose input parameters
        String functionName = "getUrl";
        String outType = "byte[]";
        RequestMessage reqMsg = new RequestMessage(functionName, outType);

        // create request message and send
        reqMsg.inParams = new InParam[1];
        reqMsg.inParams[0] = new InParam("url", "java.lang.String", url);

        // create a binary message
        byte[] reqBytes = NetUtils.serialize(reqMsg);
        client.send(functionName, reqBytes);
    }

    public void close() {
        client.close();
    }

    class RmiClient extends Client {
        public RmiClient() {
            super();
        }

        public RmiClient(String brokerIp) {
            super(brokerIp);
        }

        @Override
        public void receive(String idChain, String funcName, byte[] resp) {
            listener.dataReceived(idChain, funcName, resp);
        }
    }
}
