package com.usu.oneviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;

public class ChatActivity extends OneActivity {
    private SwipeRefreshLayout mSwipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        setTitle("Let's Chat!");
        generateActions();

        // adding components
        mSwipeRefresh = findViewById(R.id.swipeRefresh);

    }

}
