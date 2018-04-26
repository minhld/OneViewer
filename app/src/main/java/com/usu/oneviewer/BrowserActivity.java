package com.usu.oneviewer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.usu.oneviewer.net.XWebServerHelper;

import butterknife.BindView;

public class BrowserActivity extends OneActivity {
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.viewer)
    WebView viewer;

    @BindView(R.id.urlText)
    TextView urlText;

    @BindView(R.id.goBtn)
    Button goBtn;

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
                loadUrlToViewer();
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the page
                loadUrlToViewer();
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

    private void setupWebView() {
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // make sure the url is converted to local url to load within
                // the scope of the embedded WebView
                String localUrl = XWebServerHelper.openUrl(url);
                return super.shouldOverrideUrlLoading(view, localUrl);
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
    }

    private void loadUrlToViewer() {
        String url = urlText.getText().toString();
        String localUrl = XWebServerHelper.openUrl(url);
        viewer.loadUrl(localUrl);
    }
}
