package com.usu.oneviewer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.usu.oneviewer.support.MessageListAdapter;
import com.usu.oneviewer.support.UserMessage;

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
    List<UserMessage> messageList;


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
        messageList = new ArrayList<>();
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addNewMessage() {
        String msg = messageText.getText().toString();
        messageList.add(UserMessage.createMessage(msg));
        mMessageAdapter.notifyDataSetChanged();

        // reset the message text
        messageText.setText("");
    }
}
