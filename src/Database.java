import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class Database {
    static final String DB_URL = "jdbc:mysql://localhost/domotica";
    static final String USER = "root";
    static final String PASS = "root";

    private Connection conn;

    // Create a QueryRunner that will use connections
    QueryRunner run = new QueryRunner();

    void connect() {
        // Open a connection
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateOne(String query) {
        if (conn == null) connect();

        int result = 0;

        try {
            result = run.update(conn, query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Object[] queryOne(String query, String param) {
        if (conn == null) connect();

        Object[] result = new Object[0];

        try {
            if (param == null) result = run.query(conn, query, h);
            else result = run.query(conn, query, h, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Object[] queryOne(String query) {
        return queryOne(query, null);
    }

    public List<User> getUsers() {
        if (conn == null) connect();

        try {
            return run.query(conn, "SELECT * FROM users", hu);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    ResultSetHandler<List<User>> hu
            = new BeanListHandler<>(User.class);

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

    List<Map<String, Object>> queryMap(String query) {
        try {
            List<Map<String, Object>> result = run.query(
                    conn, query, new MapListHandler());
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    List<Object[]> queryArray(String query) {
        try {
            List<Object[]> result = run.query(
                    conn, query, new ArrayListHandler());
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }


    public Object[] getTemperature() {
        return queryOne("SELECT * FROM `temperature` ORDER BY id DESC LIMIT 1");
    }

    public Object[] getPressure() {
        return queryOne("SELECT * FROM `pressure` ORDER BY id DESC LIMIT 1");
    }

    public Object[] getLight() {
        return queryOne("SELECT * FROM `light` ORDER BY id DESC LIMIT 1");
    }

    public Object[] getHumidity() {
        return queryOne("SELECT * FROM `humidity` ORDER BY id DESC LIMIT 1");
    }

}

