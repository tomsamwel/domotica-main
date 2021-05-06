import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerGUI extends javax.swing.JFrame{
    private JButton previousButton;
    private JPanel controls;
    private JButton playButton;
    private JButton nextButton;

    static MusicPlayer player = MusicPlayer.getInstance();


    MusicPlayerGUI(){
        setContentPane(controls);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation

        player.loadMusic();

        this.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.isPlaying){
                    player.pause();
                    MusicPlayerGUI.this.playButton.setText("play");
                    pack();
                } else {
                    player.play();
                    MusicPlayerGUI.this.playButton.setText("pause");
                    pack();
                }
            }
        });
    }


}
