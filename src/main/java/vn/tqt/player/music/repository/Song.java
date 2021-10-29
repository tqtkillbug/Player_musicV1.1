package vn.tqt.player.music.repository;

import java.io.Serializable;

public class Song implements Serializable {
    private String songName;
    private String imagePath;
    private String songPath;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public Song() {
    }

    public Song(String songName, String imagePath, String songPath) {
        this.songName = songName;
        this.imagePath = imagePath;
        this.songPath = songPath;
    }


}


