import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.Arrays;

public class Client {

    public static String[] getSongs(){
        try {
            Socket socket = new Socket("192.168.178.25", 6669);

            OutputStream outputStream = socket.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF("");

            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            String[] s = (String[]) objectInputStream.readObject();

            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.

           // socket.close();
            return s;
        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String getLooping(){
        try {
            Socket socket = new Socket("192.168.178.25", 6668);

            OutputStream outputStream = socket.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF("");

            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            String responseMessage = reader.readLine();
            System.out.println(responseMessage);

            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.

            socket.close();
            System.out.println(responseMessage);
            return responseMessage;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String getPlaying(){
        try {
            Socket socket = new Socket("192.168.178.25", 6667);

            OutputStream outputStream = socket.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF("");

            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            String responseMessage = reader.readLine();
            System.out.println(responseMessage);

            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.

            socket.close();
            return responseMessage;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String sendRequest(String song) {
        System.out.println(2);
        try {
            Socket socket = new Socket("192.168.178.25", 6666);

            OutputStream outputStream = socket.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);


            System.out.println(song);
            dataOutputStream.writeUTF(String.valueOf(song));

            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            String responseMessage = reader.readLine();
            System.out.println(responseMessage);

            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.

            socket.close();
            return responseMessage;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }
}