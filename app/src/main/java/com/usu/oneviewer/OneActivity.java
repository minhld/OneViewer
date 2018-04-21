package com.usu.oneviewer;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class OneActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // customize the
        getSupportActionBar().setCustomView(R.layout.one_actionbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    @Override
    public void setContentView(int layoutResID) {
        // this method is override to lay the action bar below the
        // navigation bar
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        View overlayView = decorView.getChildAt(0);

        decorView.removeView(overlayView);
        getLayoutInflater().inflate(layoutResID, decorView, true);

        RelativeLayout overlayContainer = findViewById(R.id.mainLayout);
        overlayContainer.addView(overlayView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.mainItem) {
//
//        } else if (id == R.id.manageItem) {
//
//        } else if (id == R.id.settingsItem) {
//
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
