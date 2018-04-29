package com.usu.oneviewer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.usu.oneviewer.support.MessageListAdapter;
import com.usu.oneviewer.support.UserMessage;
import com.usu.oneviewer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChatActivity extends OneActivity {
    @BindView(R.id.edittext_chatbox)
    EditText messageText;

    @BindView(R.id.reyclerview_message_list)
    RecyclerView mMessageRecycler;

    @BindView(R.id.button_chatbox_send)
    Button messageSend;

    MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Let's Chat!");
        generateActions();

        createControls();
    }

    private void createControls() {
        // send button handler
        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMessage();
            }
        });

        // initiate the message recycler
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, Utils.messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addNewMessage() {
        // wont accept if the message is empty
        String msg = messageText.getText().toString();
        if (msg.trim().equals("")) return;

        Utils.addMessage(UserMessage.createMessage(msg));
        mMessageAdapter.notifyDataSetChanged();

        // reset the message text
        messageText.setText("");
    }
}
