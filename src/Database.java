import org.apache.commons.dbutils.*;
import java.sql.*;

public class Database {
    static final String DB_URL = "jdbc:mysql://localhost/domotica";
    static final String USER = "root";
    static final String PASS = "root";

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

    public Object[] queryOne(String query, String param){
        if (conn == null) connect();

        Object[] result = new Object[0];

        try{
            result = run.query(conn, query, h, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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


}

