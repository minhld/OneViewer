package com.usu.oneviewer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.usu.utils.Utils;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // prepare for turning to the Main Screen
        new SplashLoader().execute();
    }

    class SplashLoader extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Utils.sleep(Utils.DELAY_TIME);
            publishProgress();

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // close this activity
            finish();
        }
    }
}
