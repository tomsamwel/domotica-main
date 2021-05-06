import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerGUI extends javax.swing.JFrame{
    private JButton previousButton;
    private JPanel controls;
    private JButton playButton;
    private JButton nextButton;
    private JLabel songLabel;
    private JList songList;

    MusicPlayer player = new MusicPlayer();


    MusicPlayerGUI(){
        setContentPane(controls);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation


        player.loadMusic();
        songList.setListData(player.getSongs());
        refreshTexts();
        pack();

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.isPlaying){
                    player.pause();
                    playButton.setText("play");
                } else {
                    player.play();
                    playButton.setText("pause");
                }
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.previous();

                songList.setSelectedIndex(player.song);
                refreshTexts();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.next();
                songList.setSelectedIndex(player.song);
                refreshTexts();

            }
        });

        songList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (songList.getSelectedIndex() != player.song){
                    player.selectSong(songList.getSelectedIndex());
                    refreshTexts();
                }
            }
        });
    }

    void refreshTexts(){
        songLabel.setText(player.getSongName());
        songList.setSelectedIndex(player.song);
    }
}
