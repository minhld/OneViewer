package com.usu.oneviewer.support;

import com.usu.oneviewer.utils.Utils;

import java.util.Date;

public class UserMessage {
    public String message;
    public User sender;
    public long createdAt;

    public UserMessage(String msg) {
        this.message = msg;
        this.sender = Utils.currentUser;
        this.createdAt = new Date().getTime();
    }

    public static UserMessage createMessage(String msg) {
        return new UserMessage(msg);
    }
}
