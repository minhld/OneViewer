package com.usu.oneviewer.support;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usu.oneviewer.R;
import com.usu.oneviewer.utils.Utils;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<UserMessage> mMessageList;

    public MessageListAdapter(Context context, List<UserMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        UserMessage message = mMessageList.get(position);

        if (message.sender.userId.equals(Utils.currentUser.userId)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserMessage message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }


    class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(UserMessage message) {
            messageText.setText(message.message);

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.createdAt));
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(UserMessage message) {
            messageText.setText(message.message);

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.createdAt));
            nameText.setText(message.sender.nickname);

            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.sender.profileUrl, profileImage);
            profileImage.setImageBitmap(BitmapFactory.decodeResource(null, R.drawable.ic_person));
        }
    }
}
