package vn.tqt.player.music.repository;

import java.util.ArrayList;

public class Playlist {
    private String Name;
    private ArrayList<String> songInList = new ArrayList();

    public Playlist(String name, ArrayList<String> songInList) {
        Name = name;
        this.songInList = songInList;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getSongInList() {
        return songInList;
    }

    public void setSongInList(ArrayList<String> songInList) {
        this.songInList = songInList;
    }
}
