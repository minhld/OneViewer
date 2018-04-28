package com.usu.oneviewer.support;

import android.os.Build;

import java.util.UUID;

public class User {
    public String userId;
    public String nickname;
    // public String profileUrl = "";

    public User() {
        this.userId = UUID.randomUUID().toString();
        this.nickname = Build.MANUFACTURER + " " + Build.MODEL;
    }

    public User(String nickname) {
        this.userId = UUID.randomUUID().toString();
        this.nickname = nickname;
    }

    public User(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
