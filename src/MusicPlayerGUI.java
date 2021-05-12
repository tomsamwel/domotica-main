import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MusicPlayerGUI extends javax.swing.JFrame {
    private JButton previousButton;
    private JPanel controls;
    private JButton playButton;
    private JButton nextButton;
    private JLabel songLabel;
    private JList songList;
    private JButton loopButton;

    MusicPlayer player = new MusicPlayer();


    MusicPlayerGUI() {
        setContentPane(controls);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation


        player.loadMusic();
        songList.setListData(player.getSongNames());
        refreshTexts();
        pack();

        playButton.addActionListener(e -> {
            if (player.isPlaying()) {
                player.pause();
                playButton.setText("play");
            } else {
                player.play();
                playButton.setText("pause");
            }
        });
        previousButton.addActionListener(e -> {
            player.previous();

            songList.setSelectedIndex(player.song);
            refreshTexts();
        });
        nextButton.addActionListener(e -> {
            player.next();
            songList.setSelectedIndex(player.song);
            refreshTexts();

        });

        songList.addListSelectionListener(e -> {
            if (songList.getSelectedIndex() != player.song) {
                player.selectSong(songList.getSelectedIndex());
                refreshTexts();
            }
        });
        loopButton.addActionListener(e -> {
            player.toggleLooping();
            loopButton.setText(player.isLooping() ? "stop looping" : "loop");
        });
    }

    void refreshTexts() {
        songLabel.setText(player.getSongName());
        songList.setSelectedIndex(player.song);
    }
}
