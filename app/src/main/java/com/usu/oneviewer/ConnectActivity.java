package com.usu.oneviewer;

import android.content.pm.PackageManager;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.usu.connection.utils.DevUtils;
import com.usu.connection.wfd.WFDSupporter;
import com.usu.connection.wifi.WiFiManager;
import com.usu.connection.wifi.WiFiSupporter;
import com.usu.oneviewer.net.NetworkUtils;
import com.usu.tinyservice.network.NetUtils;
import com.usu.oneviewer.utils.Utils;


import butterknife.BindView;

public class ConnectActivity extends OneActivity {
    @BindView(R.id.swipeWfdRefresh)
    SwipeRefreshLayout mSwipeWfdRefresh;

    @BindView(R.id.swipeWifiRefresh)
    SwipeRefreshLayout mSwipeWifiRefresh;

    @BindView(R.id.wfdList)
    ListView mWfdList;

    @BindView(R.id.wifiList)
    ListView mWifiList;

    @BindView(R.id.infoText)
    TextView mInfoText;

    WFDSupporter wfdSupporter;
    WiFiSupporter wifiSupporter;

    Handler mainUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DevUtils.MESSAGE_GO_CONNECTED: {
                    // device becomes a server in wifi-direct model
                    WifiP2pInfo p2pInfo = (WifiP2pInfo) msg.obj;
                    NetworkUtils.wfdBrokerIp = p2pInfo.groupOwnerAddress.getHostAddress();

                    // initialize broker here
                    NetworkUtils.initBroker(NetworkUtils.wfdBrokerIp);
                    NetworkUtils.initWorker(NetworkUtils.wfdBrokerIp);
                    Utils.printLog(ConnectActivity.this, mInfoText, "Server: " + NetworkUtils.wfdBrokerIp + "\r\n");
                    break;
                }
                case DevUtils.MESSAGE_DISCONNECTED: {
                    NetworkUtils.disconnectAll();
                    break;
                }
                case DevUtils.MESSAGE_CLIENT_CONNECTED: {
                    // device becomes a client in wifi-direct model
                    WifiP2pInfo p2pInfo = (WifiP2pInfo) msg.obj;
                    NetworkUtils.wfdBrokerIp = p2pInfo.groupOwnerAddress.getHostAddress();

                    // initialize worker here
                    NetworkUtils.initClient(NetworkUtils.wfdBrokerIp);
                    // NetworkUtils.initWorker(NetworkUtils.wfdBrokerIp);
                    Utils.printLog(ConnectActivity.this, mInfoText, "Client: " + NetworkUtils.wfdBrokerIp + "\r\n");
                    break;
                }
                case DevUtils.MESSAGE_WIFI_DETECTED: {
                    WifiInfo wifiInfo = (WifiInfo) msg.obj;
                    NetworkUtils.wifiBrokerIp = DevUtils.getIPString(wifiInfo.getIpAddress());
                    break;
                }
                case DevUtils.MESSAGE_WFDLIST_UPDATED: {
                    // when the list of device is updated
                    mSwipeWfdRefresh.setRefreshing(false);
                    break;
                }
                case DevUtils.MESSAGE_WIFILIST_UPDATED: {
                    // when the list of device is updated
                    mSwipeWifiRefresh.setRefreshing(false);
                    break;
                }
                case DevUtils.MESSAGE_INFO: {
                    Utils.printLog(ConnectActivity.this, mInfoText, msg.obj);
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

        // get controls ready for use
        setupControls();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wfdSupporter.runOnPause();
        wifiSupporter.runOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wfdSupporter.runOnResume();
        wifiSupporter.runOnResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connect_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshItem: {
                // refresh both wifi and wifi-direct list
                discoverWfdPeers();
                discoverWifiNetworks();
                break;
            }
            case R.id.settingsItem: {
                // requesting group information
                wfdSupporter.requestGroupInfo();
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WiFiManager.WIFI_REQUEST_CODE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // search for Wifi network list
            wifiSupporter.getWifiConnections();
        }
    }

    private void setupControls() {
        // ------ Main Handler ------
        NetUtils.setMainHandler(mainUiHandler);
        mInfoText.setMovementMethod(new ScrollingMovementMethod());

        // ------ WIFI DIRECT ------
        // setup the wifi-direct list and its supporter
        wfdSupporter = new WFDSupporter(this);
        mWfdList.setAdapter(wfdSupporter.getDeviceListAdapter());

        // start looking for the peers
        discoverWfdPeers();

        // setup swipe component - drag down and release to REFRESH
        mSwipeWfdRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the list
                discoverWfdPeers();
            }
        });

        // ------ ORIGINAL WIFI ------
        // setup the wifi list and its supporter
        wifiSupporter = new WiFiSupporter(this);
        mWifiList.setAdapter(wifiSupporter.getWifiListAdapter());

        // start looking for the peers
        discoverWifiNetworks();

        // set up swipe
        mSwipeWifiRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the WiFi list
                discoverWifiNetworks();
            }
        });
    }

    private void discoverWfdPeers() {
        wfdSupporter.discoverPeers();
        Utils.toast(this, "Start discovering peers...");
    }

    private void discoverWifiNetworks() {
        wifiSupporter.prepRetrieveWifiConnections(this);
        // Utils.toast(this, "Start discovering WiFi networks...");
    }
}
