package com.usu.oneviewer;

import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.usu.connection.utils.DevUtils;
import com.usu.connection.wfd.WFDSupporter;
import com.usu.connection.wifi.WiFiSupporter;
import com.usu.tinyservice.network.NetUtils;

import butterknife.BindView;

public class ConnectActivity extends OneActivity {
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.wfdList)
    ListView mDeviceList;

    @BindView(R.id.loadingBar)
    ProgressBar mLoadingBar;

    WFDSupporter wfdSupporter;
    WiFiSupporter wfSupport;

    Handler mainUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DevUtils.MESSAGE_GO_CONNECTED: {
                    WifiP2pInfo p2pInfo = (WifiP2pInfo) msg.obj;
//                    brokerIp = p2pInfo.groupOwnerAddress.getHostAddress();
//                    UITools.printLog(MainActivity.this, infoText, "Server " + brokerIp);
                    break;
                }
                case DevUtils.MESSAGE_CLIENT_CONNECTED: {
                    WifiP2pInfo p2pInfo = (WifiP2pInfo) msg.obj;
                    // initWorker(p2pInfo.groupOwnerAddress.getHostAddress());
//                    brokerIp = p2pInfo.groupOwnerAddress.getHostAddress();
//                    UITools.printLog(MainActivity.this, infoText, brokerIp);
                    break;
                }
                case DevUtils.MESSAGE_WIFI_DETECTED: {
                    WifiInfo wifiInfo = (WifiInfo) msg.obj;
//                    wifiBrokerIp = DevUtils.getIPString(wifiInfo.getIpAddress());
//                    ipText.setText(wifiBrokerIp);
                    break;
                }
                case DevUtils.MESSAGE_LIST_UPDATED: {
                    // when the list of device is updated
                    mSwipeRefresh.setRefreshing(false);
                    break;
                }
                case DevUtils.MESSAGE_INFO: {
//                    UITools.printLog(MainActivity.this, infoText, msg.obj);
                    // DevUtils.printLog(MainActivity.this, infoText, msg.obj);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        setTitle("Connections");
        generateActions();

        setupNetwork();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wfdSupporter.runOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wfdSupporter.runOnResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connect_toolbar, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void setupNetwork() {
        wfdSupporter = new WFDSupporter(this);
        mDeviceList.setAdapter(wfdSupporter.getDeviceListAdapter());

        // set up the main handler
        NetUtils.setMainHandler(mainUiHandler);

        // start looking for the peers
        wfdSupporter.discoverPeers();

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the list
                wfdSupporter.discoverPeers();
                // new LoadWFDList().execute();
            }
        });

        // wfSupport = new WiFiSupporter(this);
        // mWifiList.setAdapter(wfSupport.getWifiListAdapter());
    }

}
