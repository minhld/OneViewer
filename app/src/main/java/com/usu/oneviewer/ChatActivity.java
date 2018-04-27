package com.usu.oneviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;

public class ChatActivity extends OneActivity {
    @BindView(R.id.messageText)
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Let's Chat!");
        generateActions();


    }

}
