package com.example.fileagoapplication;

public class groupdata {
    private String group_name;
    private String group_uuid;

    public groupdata(String group_name, String group_uuid) {
        this.group_name = group_name;
        this.group_uuid = group_uuid;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_uuid() {
        return group_uuid;
    }

    public void setGroup_uuid(String group_uuid) {
        this.group_uuid = group_uuid;
    }
}
