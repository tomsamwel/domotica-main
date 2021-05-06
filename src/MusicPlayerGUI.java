import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerGUI extends javax.swing.JFrame{
    private JButton previousButton;
    private JPanel controls;
    private JButton playButton;
    private JButton nextButton;
    private JLabel songLabel;

    MusicPlayer player = new MusicPlayer();


    MusicPlayerGUI(){
        setContentPane(controls);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation


        player.loadMusic();
        songLabel.setText(player.getSongName());

        this.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.isPlaying){
                    player.pause();
                    MusicPlayerGUI.this.playButton.setText("play");
                } else {
                    player.play();
                    MusicPlayerGUI.this.playButton.setText("pause");
                }
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.previous();
                songLabel.setText(player.getSongName());

            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.next();
                songLabel.setText(player.getSongName());

            }
        });
    }


}
