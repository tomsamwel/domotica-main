import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {
    static MusicPlayer player = new MusicPlayer();

    static Clip clip;

    static String filepath = "music/Alesis-Fusion-Voice-Oohs-C4.wav";
    public static long clipTimePos = 0;

    public static Boolean isPlaying = false;
    public static Boolean isLooping = false;

    private MusicPlayer(){

    }

    public static MusicPlayer getInstance(){
        return player;
    }

    public static Boolean loadMusic(){
        try {
            File musicPath = new File(filepath);

            if (musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                System.out.println("loaded");
                return true;
            }

        }catch (Exception e){
            System.out.println("failed to load");
            System.out.println(e.getMessage());
        }
        System.out.println("file doesn't exist");
        return false;
    }

    void play(){
        clip.setMicrosecondPosition(clipTimePos);
        clip.start();
        isPlaying = true;
    }

    void pause(){
        clipTimePos = clip.getMicrosecondPosition();
        clip.stop();
        isPlaying = false;
    }
}
