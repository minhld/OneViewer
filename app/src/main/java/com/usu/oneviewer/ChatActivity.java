package com.usu.oneviewer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.usu.oneviewer.net.NetworkUtils;
import com.usu.oneviewer.support.MessageListAdapter;
import com.usu.oneviewer.support.UserMessage;
import com.usu.oneviewer.utils.Utils;

import java.util.Date;

import butterknife.BindView;

public class ChatActivity extends OneActivity {
    @BindView(R.id.edittext_chatbox)
    EditText messageText;

    @BindView(R.id.reyclerview_message_list)
    RecyclerView mMessageRecycler;

    @BindView(R.id.button_chatbox_send)
    Button messageSend;

    MessageListAdapter mMessageAdapter;
    long receiveTime = new Date().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start up with layout and title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(getString(R.string.menu_chat_title));
        generateActions();

        // where controls are placed to the UI
        createControls();

        // setup the client handler
        setClientHandler();
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

        UserMessage newMsg = UserMessage.createMessage(msg);
        // Utils.addMessage(newMsg);

        NetworkUtils.client.sendMessage(newMsg, receiveTime);
        // mMessageAdapter.notifyDataSetChanged();

        // reset the message text
        messageText.setText("");
    }

    /**
     * set client handler to handle incoming messages.
     * this will be set to listen to messages automatically
     * in every single second
     */
    private void setClientHandler() {
        NetworkUtils.setClientHandler(new NetworkUtils.ClientHandler() {
            @Override
            public void responseReceived(String info) {
                // info
            }

            @Override
            public void responseReceived(UserMessage[] msgList) {
                for (UserMessage msg : msgList) {
                    Utils.addMessage(msg);
                    receiveTime = msg.createdAt;
                }

                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mMessageAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void responseReceived(byte[] data) {}
        });
    }

}
