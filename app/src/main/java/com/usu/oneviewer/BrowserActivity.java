package com.usu.oneviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.usu.oneviewer.support.RecyclerEventAdapter;
import com.usu.utils.DbHelper;
import com.usu.utils.Event;

import java.util.List;

public class BrowserActivity extends OneActivity {
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressBar mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        setTitle("Browser");
        generateActions();

        // adding components
        mSwipeRefresh = findViewById(R.id.swipeRefresh);
        mLoadingBar = findViewById(R.id.loadingBar);

    }

}
