package com.usu.oneviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;
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
                // String url = urlText.getText().toString();
                String url = "http://vietnamnet.vn/vn/the-thao/xem-truc-tiep-bong-da/ket-qua-bayern-munich-1-2-real-madrid-ket-qua-cup-c1-445677.html";
                String localUrl = XWebServerHelper.openUrl(url);
                viewer.loadUrl(localUrl);
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the page

            }
        });
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
}
