package vn.tqt.player.music.repository;

import java.io.Serializable;

public class Song implements Serializable {
    private Integer id;
    private String songName;


    public Song(Integer id, String songName) {
        this.id = id;
        this.songName = songName;
    }
    public Song(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}

