package com.example.fileagoapplication;

public class User {
    String uuid;
    String token;
    String status;
    String msg;
    String fullname;
    String fileaccesskey;
    String avatar;
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User(String uuid, String token, String status, String msg, String fullname, String fileaccesskey, String avatar) {
        this.uuid = uuid;
        this.token = token;
        this.status = status;
        this.msg = msg;
        this.fullname = fullname;
        this.fileaccesskey = fileaccesskey;
        this.avatar = avatar;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFileaccesskey() {
        return fileaccesskey;
    }

    public void setFileaccesskey(String fileaccesskey) {
        this.fileaccesskey = fileaccesskey;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
