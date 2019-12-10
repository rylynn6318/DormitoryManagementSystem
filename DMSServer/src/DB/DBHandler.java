package DB;

import java.sql.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

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

    private static final int INITIAL_CAPACITY = 10;
    ConcurrentLinkedQueue<Connection> pool = new ConcurrentLinkedQueue<>();

    public void init() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        for (int i = 0; i < INITIAL_CAPACITY; ++i) {
            pool.add(DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD));
        }
    }

    public Connection getConnection() throws SQLException {
        if (pool.isEmpty())
            pool.add(DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD));
        
        return pool.poll();
    }

    public void returnConnection(Connection connection){
        pool.add(connection);
    }

    //public static void select(Function<ISelect, T> function){
    //    Connection connection = DBHandler.INSTANCE.getConnetion();
    //    PreparedStatement preparedStatement = connection.prepareStatement();
    //}
}
