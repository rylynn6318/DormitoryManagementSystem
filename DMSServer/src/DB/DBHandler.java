package DB;

import java.sql.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum DBHandler {
    INSTANCE;

    static final String DRIVER_NAME = "mysql";
    static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
    static final String PORT = "3306";
    static final String DB_NAME = "Prototype";
    static final String USER_NAME = "admin";
    static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1";
    static final String DB_URL =
            "jdbc:" +
                    DRIVER_NAME + "://" +
                    HOSTNAME + ":" +
                    PORT + "/" +
                    DB_NAME + "?user=" +
                    USER_NAME + "&password=" +
                    PASSWORD;

    private static final int INITIAL_CAPACITY = 5;
    ConcurrentLinkedQueue<Connection> pool = new ConcurrentLinkedQueue<>();

    DBHandler() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < INITIAL_CAPACITY; ++i) {
            try {
                pool.add(DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnetion() throws SQLException {
        if (pool.isEmpty())
            pool.add(DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD));
        
        return pool.poll();
    }

    public void returnConnection(Connection connection){
        pool.add(connection);
    }
}
