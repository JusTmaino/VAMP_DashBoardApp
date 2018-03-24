package com.mbds.vamp.dashboardapp.model;

/**
 * Created by Yasmine on 15/03/2018.
 */

public class Music extends Setting {

    private int volume;
    private Playlist [] playlists;

    public Music(String name) {
        super(name);
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Playlist[] getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Playlist[] playlists) {
        this.playlists = playlists;
    }
}
