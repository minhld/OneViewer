package com.usu.oneviewer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.usu.utils.Utils;

import butterknife.ButterKnife;

public class OneActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    boolean exitFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // customize the actionbar
        getSupportActionBar().setCustomView(R.layout.one_actionbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ButterKnife.bind(this);
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
                startActivity(ConnectActivity.class);
            }
        } else if (id == R.id.browserItem) {
            if (!(this instanceof BrowserActivity)) {
                startActivity(BrowserActivity.class);
            }
        } else if (id == R.id.chatItem) {
            if (!(this instanceof ChatActivity)) {
                startActivity(ChatActivity.class);
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (exitFlag) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back button again to exit.", Toast.LENGTH_SHORT).show();
            exitFlag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitFlag = false;
                }
            }, Utils.DELAY_BACK_PRESS);

        }
    }

    protected void setTitle(String title) {
        TextView titleText = findViewById(R.id.captionText);
        titleText.setText(title);
    }

    protected void generateActions() {
        // set up the navigation bar
        // add the left side menu to the onclick handlers
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // setup the handlers for action bar's buttons
        ImageView menuImage = findViewById(R.id.openMenuImage);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void startActivity(Class<?> actClass) {
        Intent intent = new Intent(this, actClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
