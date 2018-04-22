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
    private RecyclerView mListView;
    private ProgressBar mLoadingBar;

    private RecyclerEventAdapter mEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        setTitle("Browser");
        generateActions();

        // adding components
        mSwipeRefresh = findViewById(R.id.swipeRefresh);
        mListView = findViewById(R.id.viewList);
        mLoadingBar = findViewById(R.id.loadingBar);

    }



    private void addEventHandlers() {
        // setup scroll-down refresh handler
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload the list
                new LoadList().execute();
            }
        });

        // configure the list
        StaggeredGridLayoutManager mLayoutManager =
                            new StaggeredGridLayoutManager(2,
                            StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.
                            GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(mLayoutManager);

        // event list view
//        mEventAdapter = new EventAdapter(this);
//        mListView.setAdapter(mEventAdapter);
        mEventAdapter = new RecyclerEventAdapter(this);
        mListView.setAdapter(mEventAdapter);

        // reload data
        new LoadList().execute();
    }

    /**
     * loading class
     */
    class LoadList extends AsyncTask {
        List<Event> events;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            events = DbHelper.getEvents(20);
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mLoadingBar.setVisibility(View.INVISIBLE);
//            mEventAdapter.clear();
//            mEventAdapter.addAll(events);
            mEventAdapter.updateEventList(events);
            mEventAdapter.notifyDataSetChanged();
            mSwipeRefresh.setRefreshing(false);
        }
    }

}
