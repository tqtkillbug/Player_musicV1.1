package vn.tqt.player.music;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;
import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;

public class PlayerController implements Initializable{
    @FXML
    private Pane pane;
    @FXML
    private Label singerName;
    @FXML
    private Label songName;
    @FXML
    private Label songTime;
    @FXML
    private Button playButton, pauseButton, nextButton, perviousButton, loopButton, randomButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeBar;
    @FXML
    private ProgressBar songProgressBar;

    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber;
    private int[] speeds = {75, 100, 125, 150, 175};
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private boolean playbtnstatus;
    private boolean randombtnstatus;
    private boolean loopbtnstatus;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<>();
        directory = new File("music");
        files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                songs.add(file);
                System.out.println(file);
            }
        }

        initMedia();

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]));
        }
        speedBox.setOnAction(this::changeSpeed);
        volumeBar.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeBar.getValue() * 0.01);
            }
        });
    }

    public void getSongInfo(int songIndex)  {
        String fileLocation = songs.get(songIndex).toPath().toString();
        try {
            InputStream input = new FileInputStream(fileLocation);
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            songName.setText(metadata.get("title"));
            singerName.setText(metadata.get("xmpDM:artist"));
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }
    }


    public void initMedia() {
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        getSongInfo(songNumber);

    }

    public void playSong() {
        if (playbtnstatus){
            mediaPlayer.pause();
            cancelTimer();
            playbtnstatus = false;
            playButton.setText("Play");
        } else{
            mediaPlayer.play();
            getSongInfo(songNumber);
            beginTimer();
            playbtnstatus = true;
            playButton.setText("Pause");
        }
    }

    public void nextSong() {
        if (songNumber < songs.size() - 1) {
            playbtnstatus =false;
            songNumber++;
            mediaPlayer.stop();
            initMedia();
            playSong();
        } else {
            songNumber = 0;
            mediaPlayer.stop();
            initMedia();
            playbtnstatus = false;
            playSong();
        }
    }

    public void perviousSong() {
        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
            initMedia();
            playSong();
        }
    }
    public void loopSong() {
        if (!loopbtnstatus){
            loopbtnstatus = true;
            loopButton.setText("looping");
        } else{
            loopbtnstatus = false;
            loopButton.setText("loop");
        }

    }
    public void playLoopSong() {
        if (loopbtnstatus){
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            getSongInfo(songNumber);
        }

    }
     public void randomSong(){
        if (!randombtnstatus){
            randombtnstatus = true;
            randomButton.setText("randoming");
        } else{
            randombtnstatus = false;
            randomButton.setText("random");
        }
     }
    public int randomSongNumber() {
        if (randombtnstatus){
            int max = songs.size() - 1;
            return (int)Math.floor(Math.random()*(max+1));
        }
        return -1;
    }
    public void playRandomSong(){
        mediaPlayer.stop();
        media = new Media(songs.get(randomSongNumber()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        getSongInfo(randomSongNumber());
    }

    public void changeSpeed(ActionEvent event) {
        mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()) * 0.01);
    }

    public void beginTimer() {
        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        double current = mediaPlayer.getCurrentTime().toSeconds();
                        double end = media.getDuration().toSeconds();
                        int second = (int) current % 60;
                        int minute = (int) (current / 60) % 60;
                        String minutes = String.valueOf(minute);
                        String seconds = String.valueOf(second);
                        songTime.setText(minutes +":" + seconds);
                        songProgressBar.setProgress(current/end);
                        if(current/end == 1) {
                            playLoopSong();
//                            playRandomSong();
                            cancelTimer();
                        }
                    }
                });

            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private void cancelTimer() {
        running = false;
        timer.cancel();
    }


}