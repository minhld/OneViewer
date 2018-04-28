package com.usu.oneviewer.support;

public class User {
    public String userId;
    public String nickname;
    public String profileUrl;

    public User(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileUrl = "";
    }
}
