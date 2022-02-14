package com.example.fileagoapplication;
public class data {
    private String name;
    private String uuid;
    private String type;
    public data(String name, String uuid,String type) {
        this.name = name;
        this.uuid = uuid;
        this.type=type;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
