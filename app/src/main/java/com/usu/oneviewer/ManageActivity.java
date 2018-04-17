package com.usu.oneviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.usu.oneviewer.support.RecyclerEventAdapter;
import com.usu.utils.DbHelper;
import com.usu.utils.Event;

import java.util.List;

public class ManageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mListView;
    private ProgressBar mLoadingBar;

    private RecyclerEventAdapter mEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mListView = (RecyclerView) findViewById(R.id.viewList);
        mLoadingBar = (ProgressBar) findViewById(R.id.loadingBar);

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
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,
                                                StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mainItem) {

        } else if (id == R.id.manageItem) {

        } else if (id == R.id.settingsItem) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
