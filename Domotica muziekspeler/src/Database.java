import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Database {
    static final String DB_URL = "jdbc:mysql://localhost/domotica";
    static final String USER = "root";
    static final String PASS = "";

    private Connection conn;

    // Create a QueryRunner that will use connections
    QueryRunner run = new QueryRunner();

    void connect(){
        // Open a connection
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Connection connecti(){
        // Open a connection
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean newPlaylist(int param, String param2){
        String query = "INSERT INTO playlists (userID, name) VALUES (?, ?)";

        if (conn == null) connect();

        List<Object[]> result = Collections.singletonList(new Object[0]);

        try{
            result = run.execute(conn, query, h, param, param2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        System.out.println(result);
        return true;
    };

    public Object[] queryOne(String query, String param){
        if (conn == null) connect();

        Object[] result = new Object[0];

        try{
            if (param == null) result = run.query(conn, query, h);
            else result = run.query(conn, query, h, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<String> getPlaylists(int userID) {
        String query = "SELECT id, name FROM playlists WHERE userID = " + userID;
        if (conn == null) connect();

        ArrayList<String> result;

        try{
            result = queryArray(query);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getUserID(String param){
        String userIDString = Arrays.toString(queryOne("SELECT id FROM users WHERE name = ?", param));
        return Integer.parseInt(userIDString.substring(1, userIDString.length()-1));
    }

    public ArrayList<String> getPlaylistSongs(int userID, Object selectedItem) {
        System.out.println(selectedItem);
        System.out.println(selectedItem);
        String query = "SELECT id FROM playlists WHERE userID = ? AND name = ?";
        if (conn == null) connect();

        Object[] result = new Object[0];

        try{
            result = run.query(conn, query, h, userID, selectedItem);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(result == null){
            return null;
        }
        query = "SELECT songID FROM playlists_songs WHERE playlistID = '" + result[0] + "'";
        ArrayList<String> ids = queryArray(query);

        ArrayList<String> songnames = new ArrayList<>();

        for (int x = 0; x < ids.toArray().length; x++){
            query = "SELECT name FROM song WHERE id = '" + ids.get(x).substring(1, ids.get(x).length() - 1) + "'";
            String res = Arrays.toString(queryOne(query));
            songnames.add(res.substring(1, res.length() - 1));

        }
        return songnames;
    }

    public void addSongToPlaylist(String playlistName, String songName, int userID) {

        int songID;
        int playlistID;

        String query = "SELECT id FROM playlists WHERE userID = ? AND name = ?";
        if (conn == null) connect();

        Object[] result = new Object[0];

        try{
            result = run.query(conn, query, h, userID, playlistName);
            playlistID = (int) result[0];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        query = "SELECT id FROM song WHERE name = ?";

        try{
            result = run.query(conn, query, h, songName);
            songID = (int) result[0];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        query = "INSERT INTO playlists_songs (playlistID, songID) VALUES (?, ?)";

        try{
            run.execute(conn, query, h, playlistID, songID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Object[] queryOne(String query){
        return queryOne(query, null);
    }


    public ArrayList<String> queryArray(String query) {
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

    // Create a ResultSetHandler implementation to convert the
    // first row into an Object[].
    ResultSetHandler<Object[]> h = rs -> {
        if (!rs.next()) {
            return null;
        }

        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    };

    public Object[] getTemperature(){
        return queryOne("SELECT * FROM `temperature` ORDER BY id DESC LIMIT 1");
    }

    public Object[] getPressure(){
        return queryOne("SELECT * FROM `pressure` ORDER BY id DESC LIMIT 1");
    }

    public Object[] getLight(){
        return queryOne("SELECT * FROM `light` ORDER BY id DESC LIMIT 1");
    }

    public Object[] getHumidity(){
        return queryOne("SELECT * FROM `humidity` ORDER BY id DESC LIMIT 1");
    }


    public void removeSong(Object playlist, String songName, int userID) {
        String query = "SELECT id FROM song WHERE name = ?";
        Object[] result = new Object[0];
        int songID;
        int playlistID;
        if (conn == null) connect();

        try{
            result = run.query(conn, query, h, songName);
            songID = (int) result[0];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        query = "SELECT id FROM playlists WHERE userID = ? AND name = ?";
        System.out.println(userID);
        try{
            result = run.query(conn, query, h, userID, playlist);
            playlistID = (int) result[0];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        query = "DELETE FROM playlists_songs WHERE playlistID = ? AND songID = ?";

        try{
            run.execute(conn, query, h, playlistID, songID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePlaylist(Object playlist, int userID) {
        String query = "SELECT id FROM playlists WHERE userID = ? AND name = ?";
        Object[] result = new Object[0];
        int playlistID;
        if (conn == null) connect();

        System.out.println(userID);
        try{
            result = run.query(conn, query, h, userID, playlist);
            playlistID = (int) result[0];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        query = "DELETE FROM playlists_songs WHERE playlistID = ?";

        try{
            run.execute(conn, query, h, playlistID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        query = "DELETE FROM playlists WHERE id = ?";

        try{
            run.execute(conn, query, h, playlistID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

