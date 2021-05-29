import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        MusicPlayerGUI gui = new MusicPlayerGUI();
        Object[] songs = gui.getAllSongs().toArray();
        for (int x = 0; x < songs.length; x++){
            String s = String.valueOf(songs[x]);
            s = s.substring(s.indexOf(",") + 2, s.length()-1);
            gui.addToSongsComboBox(s);
        }Database db = new Database();
        db.connect();
        Object[] users = db.queryArray("SELECT name FROM users").toArray();
        for (int x = 0; x < users.length; x++){
            String s = String.valueOf(users[x]);
            s = s.substring(s.indexOf(",") + 2, s.length()-1);

            gui.addToComboBox(s);
        }
    }
}
