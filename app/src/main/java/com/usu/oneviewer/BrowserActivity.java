package com.usu.oneviewer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.usu.oneviewer.net.XWebServerHelper;

import java.util.Stack;

import butterknife.BindView;

public class BrowserActivity extends OneActivity {
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.viewer)
    WebView viewer;

    @BindView(R.id.urlText)
    EditText urlText;

    @BindView(R.id.goBtn)
    Button goBtn;

    Menu optionMenu;
    Stack<String> historyStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        setTitle("Browser");
        generateActions();

        // adding components
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrlToViewer("", true);
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the page
                loadUrlToViewer("", true);
            }
        });

        // setup WebView to fit the local web server
        setupWebView();

        // set the default value for the text box
        String defUrl = "http://www.vogella.com/contact.html";
        urlText.setText(defUrl);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XWebServerHelper.stopServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        XWebServerHelper.startServer(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.browser_toolbar, menu);
        showNetworkStatus(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backItem: {
                // go to previous page
                if (!historyStack.empty()) {
                    String lastUrl = historyStack.pop();
                    loadUrlToViewer(lastUrl, false);
                }
                break;
            }
            case R.id.refreshItem: {
                // refresh current page
                break;
            }
            case R.id.statusItem: {
                // network status connected/disconnected
                break;
            }
        }
        return false;
    }

    private void setupWebView() {
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // make sure the url is converted to local url to load within
                // the scope of the embedded WebView
                loadUrlToViewer(url, true);
                return true; // super.shouldOverrideUrlLoading(view, localUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mSwipeRefresh.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        viewer.setWebChromeClient(new WebChromeClient() {

        });

        viewer.getSettings().setJavaScriptEnabled(true);
        viewer.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showNetworkStatus(boolean isActive) {
        optionMenu.getItem(2).setIcon(
                isActive ? R.drawable.ic_wifi : R.drawable.ic_wifi_off);
    }

    /**
     * get URL from the address bar and load to the WebView.
     * It will also save URL to History Stack
     */
    private void loadUrlToViewer(String newUrl, boolean isNew) {
        // whether to save URL to History
        String lastUrl = urlText.getText().toString();
        if (isNew && historyStack.empty() ||
            isNew && !historyStack.peek().equals(lastUrl))
            historyStack.push(lastUrl);

        // go to the new URL
        if (newUrl.equals("")) {
            newUrl = urlText.getText().toString();
        }
        urlText.setText(newUrl);
        String localUrl = XWebServerHelper.openUrl(newUrl);
        viewer.loadUrl(localUrl);
    }
}
