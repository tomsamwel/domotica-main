import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class Database {
    static final String DB_URL = "jdbc:mysql://localhost/domotica";
    static final String USER = "root";
    static final String PASS = "root";
    // Create a QueryRunner that will use connections
    QueryRunner run = new QueryRunner();
    ResultSetHandler<User> userHandler = new BeanHandler<>(User.class);
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
    private Connection conn;

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

        try {
            return run.update(conn, query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public User newUser(String name) {
        if (conn == null) connect();

        try {
            return run.insert(conn, "INSERT INTO `users` (name) VALUES (?)", userHandler, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object[] queryOne(String query, String param) {
        if (conn == null) connect();

        Object[] result = new Object[0];

        try {
            if (param == null) return run.query(conn, query, h);
            else return run.query(conn, query, h, param);
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


    List<Object[]> queryArray(String query) {
        try {
            return run.query(
                    conn, query, new ArrayListHandler());
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

