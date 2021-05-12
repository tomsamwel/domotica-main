import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {
    static Clip clip;

    static String musicPath = "music";
    static File[] songs;
    public int song = 0;
    public static long clipTimePos = 0;

    public static Boolean isPlaying = false;
    public static Boolean isLooping = true;

    MusicPlayer() {


    }

    public void loadMusic() {
        try {
            songs = new File(musicPath).listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
            selectSong(0);
        } catch (Exception e) {
            System.out.println("failed to load");
            System.out.println(e.getMessage());
        }
        System.out.println("file doesn't exist");
    }

    public void selectSong(int index) {
        if (isPlaying) {
            clip.stop();
        }
        clipTimePos = 0;

        try {
            song = index;

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(songs[song]);
            clip = AudioSystem.getClip();
            clip.open(audioInput);

            if (isPlaying) play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void next() {
        if (song + 1 >= songs.length) selectSong(0);
        else selectSong(song + 1);
    }

    public void previous() {
        if (song - 1 < 0) selectSong(songs.length - 1);
        else selectSong(song - 1);
    }

    void play() {
        if (isLooping) clip.loop((int) clip.getMicrosecondPosition());
        else {
            clip.setMicrosecondPosition(clipTimePos);
            clip.start();
        }

        isPlaying = true;
    }

    void pause() {
        clipTimePos = clip.getMicrosecondPosition();
        clip.stop();
        isPlaying = false;
    }

    public String getSongName() {
        String filename = songs[song].getName();
        return filename.substring(0, filename.length() - 4);
    }

    public String getSongName(int song) {
        String filename = songs[song].getName();
        return filename.substring(0, filename.length() - 4);
    }

    public String[] getSongNames() {
        String[] songNames = new String[songs.length];

        for (int i = 0; i < songs.length; i++) {
            songNames[i] = getSongName(i);
        }
        return songNames;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public static void setLooping(Boolean isLooping) {
        MusicPlayer.isLooping = isLooping;
    }

    public static void toggleLooping() {
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
