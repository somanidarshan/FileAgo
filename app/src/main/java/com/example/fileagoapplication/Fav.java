package com.example.fileagoapplication;

public class Fav {
    private boolean starred=false;

    public Fav(boolean starred) {
        this.starred = starred;
    }
    public boolean isStarred() {
        return starred;
    }
    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
