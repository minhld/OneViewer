package com.usu.oneviewer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OneActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // customize the actionbar
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

        if (id == R.id.connectItem) {
            if (!(this instanceof ConnectActivity)) {
                Intent intent = new Intent(this, ConnectActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.browserItem) {
            if (!(this instanceof BrowserActivity)) {
                Intent intent = new Intent(this, BrowserActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.chatItem) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void setTitle(String title) {
        TextView titleText = findViewById(R.id.captionText);
        titleText.setText(title);
    }

    protected void generateActions() {
        drawer = findViewById(R.id.drawer_layout);

        ImageView menuImage = findViewById(R.id.openMenuImage);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }
}
