package com.example.fileagoapplication;

import java.util.ArrayList;

public class PersonalWorkspace {
    private String status;
    private String  msg;
    private ArrayList<data> data;

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

    public ArrayList<data> getData() {
        return data;
    }

    public void setData(ArrayList<data> data) {
        this.data = data;
    }
}
