package com.mbds.vamp.dashboardapp.model;

public class Playlist {

    String name;
    Media [] playlist;


    public Playlist(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Media[] getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Media[] playlist) {
        this.playlist = playlist;
    }

}
