import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import java.io.File;

public class MusicPlayer {
    Clip clip;


    String musicPath = "music/";
    String currentSong;
    File[] songs;
    public String song;
    public long clipTimePos = 0;

    public Boolean isPlaying = false;
    public Boolean isLooping = false;

    public void loadMusic() {
        try {
            songs = new File(musicPath).listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
        } catch (Exception e) {
            System.out.println("failed to load");
            System.out.println(e.getMessage());
        }
    }

    public void selectSong(String song) {
        if (isPlaying) {
            clip.stop();
        }
        //clipTimePos = 0;

        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(musicPath + song + ".wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInput);

            //if (isPlaying) play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void play() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("play islooping = " + isLooping);
        if (isLooping) {
            clip.loop((int) clip.getMicrosecondPosition());
            System.out.println(2);

        }
        else {
            System.out.println("clipTimePos = " + clipTimePos);
            clip.setMicrosecondPosition(clipTimePos);
            clip.start();
        }

        isPlaying = true;


        Thread t1 = new Thread(() -> {
            boolean a = true;
            while(a){
                clipTimePos = clip.getMicrosecondPosition();
                if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()){
                    System.out.println("Clip ended");
                    isPlaying = false;
                    clipTimePos = 0;
                }
                if (!isPlaying){
                    a = false;

                }

            }
        });
        t1.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        isPlaying = false;

        clipTimePos = clip.getMicrosecondPosition();
        System.out.println(clip.getMicrosecondPosition());
        System.out.println(clipTimePos);
        clip.stop();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(Boolean isLooping) {
        this.isLooping = isLooping;
    }

    public void toggleLooping() {
        if (isPlaying) {
            if (!isLooping) clip.loop((int) clip.getMicrosecondPosition());
            else {
                clipTimePos = clip.getMicrosecondPosition();
                clip.stop();
                clip.setMicrosecondPosition(clipTimePos);
                clip.start();
            }
        } else isLooping = !isLooping;
    }
}
