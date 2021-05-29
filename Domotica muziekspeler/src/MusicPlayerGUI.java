import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MusicPlayerGUI extends javax.swing.JFrame {
    private String selectedPlaylistName;
    private String selectedIndexName;
    private JButton previousButton;
    private JPanel controls;
    private JButton playButton;
    private JButton nextButton;
    private JList<String> songList;
    private JButton loopButton;
    private JComboBox<Object> nameComboBox;
    private JComboBox<Object> playlistComboBox;
    private JButton addPlaylistButton;
    private JButton addSongButton;
    private JTextField playlistNameField;
    private JComboBox<Object> songAddComboBox;
    private JButton removeSelectedSongButton;
    private JButton deletePlaylistButton;
    private String[] songListArray;
    private int userID;
    private int selectedIndex = 0;

    public void loadPlaylists(Object selectedItem) {
        Database db = new Database();

        Object[] songs = db.getPlaylists((Integer) selectedItem).toArray();
        playlistComboBox.removeAllItems();
        for (int x = 0; x < songs.length; x++) {
            String s = String.valueOf(songs[x]);
            s = s.substring(s.indexOf(",") + 2, s.length() - 1);
            addToPlaylistComboBox(s);
        }
    }

    public ArrayList<String> getAllSongs() {
        QueryRunner run = new QueryRunner();
        Database database = new Database();
        Connection conn = database.connecti();
        String query = "SELECT name FROM song";
        try {
            ArrayList<String> ar = new ArrayList<String>();
            List<Object[]> results = run.query(conn, query, new ArrayListHandler());
            for (Object[] objects : results) {
                ar.add(Arrays.toString(objects));
            }
            return ar;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void loadSongs(Object selectedItem) {
        Database db = new Database();
        songListArray = null;
        if (selectedItem == null) {
            songList.setListData(new String[0]);
            return;
        }
        Object[] songs = db.getPlaylistSongs(userID, selectedItem).toArray();
        ArrayList<String> a = new ArrayList<String>();
        for (int x = 0; x < songs.length; x++) {

            a.add(String.valueOf(songs[x]));
        }
        songListArray = a.toArray(new String[0]);
        songList.setListData(songListArray);
    }

    private void addToPlaylistComboBox(Object song) {
        playlistComboBox.addItem(song);
    }


    MusicPlayerGUI() {
        setContentPane(controls);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation

        nameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database database = new Database();
                userID = database.getUserID((String) Objects.requireNonNull(nameComboBox.getSelectedItem()));
                loadPlaylists(userID);
            }
        });

        playlistComboBox.addActionListener(e -> {
            loadSongs(playlistComboBox.getSelectedItem());
            selectedPlaylistName = String.valueOf(playlistComboBox.getSelectedItem());
        });

        addSongButton.addActionListener(e -> {
            String songName = String.valueOf(songAddComboBox.getSelectedItem());
            if (songName != null) {
                Database database = new Database();
                database.addSongToPlaylist(selectedPlaylistName, songName, userID);
                loadSongs(playlistComboBox.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(controls, "Something went wrong!");
            }
        });

        addPlaylistButton.addActionListener(e -> {
                    String playlistName = playlistNameField.getText();
                    if (!playlistName.equals("")) {
                        Database database = new Database();

                        database.newPlaylist(userID, playlistName);
                        loadPlaylists(userID);
                    } else {
                        JOptionPane.showMessageDialog(controls, "You have to enter a playlist name!");
                    }
                }
        );

        playButton.addActionListener(e -> {
            Thread req = new Thread(() -> {
                Client.sendRequest(selectedIndexName);
            });

            req.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            Thread waitPlaying = new Thread(() -> {
                playButton.setText("pause");
                playButton.repaint();
                boolean play = true;
                while (play) {
                    String a = Client.getPlaying();
                    System.out.println(a);

                    assert a != null;
                    if (a.equals("false")) {
                        play = false;
                        playButton.setText("play");
                    }
                }
            });
            waitPlaying.start();

            playButton.setText("pause");
            playButton.repaint();
        });

        removeSelectedSongButton.addActionListener(e -> {
            Database database = new Database();

            database.removeSong(playlistComboBox.getSelectedItem(), songList.getSelectedValue(), userID);
            loadSongs(playlistComboBox.getSelectedItem());
        });

        deletePlaylistButton.addActionListener(e -> {
            Database database = new Database();

            database.removePlaylist(playlistComboBox.getSelectedItem(), userID);
            loadPlaylists(userID);
            loadSongs(playlistComboBox.getSelectedItem());
        });

        previousButton.addActionListener(e -> {
            if (selectedIndex - 1 >= 0) {
                selectedIndex--;
            }

            songList.setSelectedIndex(selectedIndex);
            refreshTexts();

        });

        nextButton.addActionListener(e -> {
            if (selectedIndex + 1 <= songList.getModel().getSize() - 1) {
                selectedIndex++;

            }
            songList.setSelectedIndex(selectedIndex);
            refreshTexts();

        });

        songList.addListSelectionListener(e -> {

            selectedIndexName = songList.getSelectedValue();
            selectedIndex = songList.getSelectedIndex();
        });

        loopButton.addActionListener(e -> {
            boolean loopBool = Boolean.parseBoolean(Client.getLooping());
            loopButton.setText(loopBool ? "Stop looping" : "Loop");
        });

        setVisible(true);

    }


    void refreshTexts() {
        //Client.sendRequest(player.song);
        // songLabel.setText(player.getSongName());
        // songList.setSelectedIndex(player.song);

    }

    public void addToComboBox(Object user) {
        nameComboBox.addItem(user);
    }

    public void addToSongsComboBox(Object song) {
        songAddComboBox.addItem(song);
    }
}
