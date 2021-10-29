package vn.tqt.player.music;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
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

public class PlayerController implements Initializable {
    @FXML
    private ImageView logoSong;
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
    private File musicDirectory;
    @FXML
    private File imageDirectory;
    private File[] musicFiles;
    private File[] imageFiles;
    private ArrayList<File> songs;
    private ArrayList<File> images;
    private int songNumber;
    private String songTitle;
    private int[] speeds = {75, 100, 125, 150, 175, 500000};
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private boolean playbtnstatus;
    private boolean randombtnstatus;
    private boolean loopbtnstatus;

    public PlayerController() throws MalformedURLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<>();
        musicDirectory = new File("music");
        musicFiles = musicDirectory.listFiles();
        images = new ArrayList<>();
        imageDirectory = new File("image");
        imageFiles = imageDirectory.listFiles();


        if (musicFiles != null) {
            for (File file : musicFiles) {
                songs.add(file);
                System.out.println(file);
            }
        }
        if (imageFiles != null) {
            for (File file : imageFiles) {
                images.add(file);
                System.out.println(file);
            }
        }
        getSongTitle();
        initMedia(songNumber);
        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]));
        }
        speedBox.setOnAction(this::changeSpeed);
        volumeBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeBar.getValue() * 0.01);
            }
        });
    }

    public String getSongTitle() {
            return songs.get(songNumber).getName();
    }

    public void getSongInfo(int songIndex) {
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

    public void initMedia(int songIndex) {
        media = new Media(songs.get(songIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        getSongInfo(songNumber);
    }

    public String getRelativeName(){
        String songNamePath = getSongTitle();
        String relativeSongName =  songNamePath.substring(0, songNamePath.length() - 4 );
        return relativeSongName + ".jpg";

    }
   public void setLogoSong() throws MalformedURLException {
       File file = new File("C:\\Users\\TienTran_LAPTOP\\IdeaProjects\\Player-music-v1.1\\src\\main\\resources\\vn\\tqt\\player\\music\\image\\" + getRelativeName());
       String localUrl = file.toURI().toURL().toString();
       Image image = new Image(localUrl);
       logoSong.setImage(image);
    }
    public void playSong() throws MalformedURLException {
//        Song song = new Song("a", "b", "c");
//        String json = JacksonParser.INSTANCE.toJson(song);
//        System.out.println(json);
//        Song s1 = JacksonParser.INSTANCE.toObject(json, Song.class);
        if (playbtnstatus) {
            mediaPlayer.pause();
            cancelTimer();
            playbtnstatus = false;
            playButton.setText("Play");
        } else {
            mediaPlayer.play();
            setLogoSong();
            getSongInfo(songNumber);
            beginTimer();
            playbtnstatus = true;
            playButton.setText("Pause");
        }
    }


    public void nextSong() throws MalformedURLException {
        if (songNumber < songs.size() - 1) {
            playbtnstatus = false;
            songNumber++;
            mediaPlayer.stop();
            initMedia(songNumber);
            playSong();
        } else {
            songNumber = 0;
            mediaPlayer.stop();
            initMedia(songNumber);
            playbtnstatus = false;
            playSong();
        }
    }

    public void perviousSong() throws MalformedURLException {
        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
            initMedia(songNumber);
            playSong();
        }
    }

    public void loopSong() {
        if (!loopbtnstatus) {
            loopbtnstatus = true;

            mediaPlayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                }
            });
            loopButton.setText("looping");
            if (randombtnstatus) {
                randombtnstatus = false;
                randomButton.setText("random");
            }
        } else {
            loopbtnstatus = false;
            loopButton.setText("loop");
        }

    }

    public void randomSong() {
        if (!randombtnstatus) {
            randombtnstatus = true;
            randomButton.setText("randoming");
            if (loopbtnstatus) {
                loopbtnstatus = false;
                loopButton.setText("loop");
            }
        } else {
            randombtnstatus = false;
            randomButton.setText("random");
        }
    }

    public int randomSongNumber() {
        if (randombtnstatus) {
            int max = songs.size() - 1;
            return (int) Math.floor(Math.random() * (max + 1));
        }
        return -1;
    }

    public void playRandomSong() {
        mediaPlayer.stop();
        songNumber = randomSongNumber();
        initMedia(songNumber);
        mediaPlayer.play();
        getSongInfo(songNumber);
        beginTimer();
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
                        songTime.setText(minutes + ":" + seconds);
                        songProgressBar.setProgress(current / end);
                        if (current / end == 1) {
                            if (randombtnstatus) {
                                playRandomSong();
                            }
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