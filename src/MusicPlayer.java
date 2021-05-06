import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FilenameFilter;

public class MusicPlayer {
    static Clip clip;

    static String musicPath = "music";
    static File songs[];
    public int song = 0;
    public static long clipTimePos = 0;

    public static Boolean isPlaying = false;
    public static Boolean isLooping = true;

    MusicPlayer(){


    }

    public Boolean loadMusic(){
        try {
            songs = new File(musicPath).listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".wav");
                }
            });
            selectSong(0);
            return true;
        }catch (Exception e){
            System.out.println("failed to load");
            System.out.println(e.getMessage());
        }
        System.out.println("file doesn't exist");
        return false;
    }

    private void selectSong(int index){
        try {
            System.out.println(songs[song]);
            song = index;

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(songs[song]);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void next(){
        clip.stop();

        if (song + 1 >= songs.length){
            selectSong(0);
        } else selectSong(song+1);
        clipTimePos = 0;

        if (isPlaying) play();
    }

    public void previous(){
        clip.stop();

        if (song - 1 < 0) selectSong(songs.length - 1);
        else selectSong(song-1);
        clipTimePos = 0;

        if (isPlaying) play();
    }

    void play(){
        if (isLooping) clip.loop((int) clip.getMicrosecondPosition());
        else {
            clip.setMicrosecondPosition(clipTimePos);
            clip.start();
        }

        isPlaying = true;
    }

    void pause(){
        clipTimePos = clip.getMicrosecondPosition();
        clip.stop();
        isPlaying = false;
    }

    public String getSongName(){
        String filename = songs[song].getName();
        return filename.substring(0, filename.length()-4);
    }
}
