package com.example.fileagoapplication;

import java.util.ArrayList;

public class GroupWorkspace {
    private String status;
    private String  msg;
    private ArrayList<groupdata> data;

    public ArrayList<groupdata> getData() {
        return data;
    }

    public void setData(ArrayList<groupdata> data) {
        this.data = data;
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

}
