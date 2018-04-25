package com.usu.connection.wifi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.usu.connection.utils.DevUtils;
import com.usu.tinyservice.network.NetUtils;

import java.util.List;

/**
 * Created by minhld on 7/26/2016.
 */
public class WiFiManager extends BroadcastReceiver {
    public static final int WIFI_REQUEST_CODE = 1;
    public static final String SERVER_IP = "144.39.162.153"; //"144.39.248.223"; //"144.39.215.135"; //"144.39.254.119";// "192.168.49.1";
    public static final String PASSWORD = "DIWNZzzv"; // "qVK5TkO9"; // "NbNCLPiX"; // "EWat5Nhr";

    android.net.wifi.WifiManager mWifiManager;
    WiFiScanListener scanListener;
    IntentFilter mIntentFilter;

    public void setWifiScanListener(WiFiScanListener scanListener) {
        this.scanListener = scanListener;
    }

    public WiFiManager(Activity c) {
        mWifiManager = (android.net.wifi.WifiManager) c.getApplicationContext().
                                    getSystemService(Context.WIFI_SERVICE);
        // IntentFilter filters = new IntentFilter();
        // filters.addAction();
        // c.registerReceiver(mWifiScanReceiver, filters);
    }

    public IntentFilter getSingleIntentFilter() {
        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            mIntentFilter.addAction(android.net.wifi.WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        }
        return mIntentFilter;
    }

    /**
     * receive a wifi network list
     */
    @Override
    public void onReceive(Context c, Intent intent) {
        if (intent.getAction().equals(android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            List<ScanResult> mScanResults = mWifiManager.getScanResults();
            if (scanListener != null) {
                // only keep wifi direct network
                for (int i = mScanResults.size() - 1; i >= 0; i--) {
                    if (!mScanResults.get(i).SSID.toLowerCase().contains("direct")) {
                        mScanResults.remove(i);
                    }
                }
                scanListener.listReceived(mScanResults);
            }
        } else if (intent.getAction().equals(android.net.wifi.WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            SupplicantState state = intent.getParcelableExtra(android.net.wifi.WifiManager.EXTRA_NEW_STATE);
            if (SupplicantState.isValidState(state) && state == SupplicantState.COMPLETED) {
                // when a wifi connection is established
                // boolean connected = checkConnectedToDesiredWifi(c);
                checkConnectedToDesiredWifi(c);
            }
        }
    }

    public void prepRetrieveWifiConnections(Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                                        PackageManager.PERMISSION_GRANTED) {
            // Android - version 23 or later
            activity.requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                                        WIFI_REQUEST_CODE);
        }else{
            // Android the lower version
            getWifiConnections();
        }

        // ActivityCompat.requestPermissions(activity,
        //         new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
    }

    public void getWifiConnections() {
        mWifiManager.startScan();
    }

    public boolean connectWifiNetwork(String SSID) {
        WifiConfiguration configuration = createOpenWifiConfiguration(SSID);
        int networkId = mWifiManager.addNetwork(configuration);
        writeLog("networkId assigned while adding network is " + networkId);
        return enableNetwork(SSID, networkId);
    }

    private WifiConfiguration createOpenWifiConfiguration(String SSID) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = String.format("\"%s\"", SSID);
        configuration.preSharedKey = "\"" + PASSWORD + "\"";
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        assignHighestPriority(configuration);
        return configuration;
    }

    /**
     * to tell OS to give preference to this network
     *
     * @param config
     */
    private void assignHighestPriority(WifiConfiguration config) {
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (config.priority <= existingConfig.priority) {
                    config.priority = existingConfig.priority + 1;
                }
            }
        }
    }

    private boolean enableNetwork(String SSID, int networkId) {
        if (networkId == -1) {
            networkId = getExistingNetworkId(SSID);

            if (networkId == -1) {
                writeLog("Couldn't add network with SSID: " + SSID);
                return false;
            }
        }
        return mWifiManager.enableNetwork(networkId, true);
    }

    private int getExistingNetworkId(String SSID) {
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (SSID != null && SSID.equals("\"" + existingConfig.SSID + "\"")) {
                    return existingConfig.networkId;
                }
            }
        }
        return -1;
    }

    /**
     * detect you are connected to a specific network.
     *
     * @param c
     */
    public WifiInfo checkConnectedToDesiredWifi(Context c) {
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager)
                                    c.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifi = wifiManager.getConnectionInfo();
        if (wifi != null) {
            writeLog("connected to: " + wifi.getSSID() + "; " +
                    "bssid: " + wifi.getBSSID() + "; " +
                    "IP: " + DevUtils.getIPString(wifi.getIpAddress()) + "; " +
                    "freq: " + wifi.getFrequency() + "MHz; " +
                    "speed: " + wifi.getLinkSpeed() + "MBps; ");
        }
        return wifi;
    }

    /**
     *
     */
    public interface WiFiScanListener {
        void listReceived(List<ScanResult> mScanResults);
    }

    public void writeLog(final String msg){
        NetUtils.print(msg);
    }

    /**
     * demonstrate the way to scan the wifi list
     * to enable wifi scanner, "Location" must be enabled
     *
     * @param c
     */
    public void getSimpleWifiList(Activity c) {
        final WifiManager mWifiManager = (WifiManager) c.getApplicationContext().
                                            getSystemService(Context.WIFI_SERVICE);

        if(mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

            // register WiFi scan results receiver
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

            c.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    List<ScanResult> results = mWifiManager.getScanResults();
                    final int N = results.size();

                    for (int i = 0; i < N; ++i) {
                        /*
                        Log.v(TAG, "  BSSID       =" + results.get(i).BSSID);
                        Log.v(TAG, "  SSID        =" + results.get(i).SSID);
                        Log.v(TAG, "  Capabilities=" + results.get(i).capabilities);
                        Log.v(TAG, "  Frequency   =" + results.get(i).frequency);
                        Log.v(TAG, "  Level       =" + results.get(i).level);
                        Log.v(TAG, "---------------");
                        */
                    }
                }
            }, filter);

            // start WiFi Scan
            mWifiManager.startScan();
        }
    }
}
