import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class server {
    public static void main(String[] args) {

        int port = 6666;
        int port2 = 6667;
        int port3 = 6668;

        MusicPlayer player = new MusicPlayer();

        Thread server1 = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {

                player.loadMusic();

                while (true) {
                    Socket socket = serverSocket.accept();

                    InputStream inputStream = socket.getInputStream();

                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String message = dataInputStream.readUTF();

                    if (player.currentSong != message) {
                        player.selectSong(message);
                    }
                    System.out.println("playing = " + player.isPlaying());
                    if (player.isPlaying()) {
                        player.pause();
                    } else {
                        player.play();
                    }

                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    writer.println(player.isPlaying());
                }

            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Thread server2 = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port2)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    writer.println(player.isPlaying());
                }

            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Thread server3 = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port3)) {
                while (true) {
                    Socket socket = serverSocket.accept();

                    player.toggleLooping();

                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    writer.println(player.isLooping());
                }

            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        server1.start();
        server2.start();
        server3.start();
    }
}
