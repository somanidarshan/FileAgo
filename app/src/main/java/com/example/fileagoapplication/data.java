package com.example.fileagoapplication;
public class data {
    private String name;
    private String uuid;
    private String type;
    private Long updated;
    private long size;
    public data(String name, String uuid,String type,Long updated,long size) {
        this.name = name;
        this.uuid = uuid;
        this.type=type;
        this.updated=updated;
        this.size=size;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Long getUpdated(){
        return updated;
   }

    public void setUpdated(Long updated) {
        this.updated = updated;
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
