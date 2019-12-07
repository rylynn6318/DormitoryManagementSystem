package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum DBHandler {
    INSTANCE;

    private static final String DRIVER_NAME = "mysql";
    private static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DB_NAME = "Prototype";													//DB이름
    private static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
    private static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
    private static final String DB_URL =
            "jdbc:" +
                    DRIVER_NAME + "://" +
                    HOSTNAME + ":" +
                    PORT + "/" +
                    DB_NAME + "?user=" +
                    USER_NAME + "&password=" +
                    PASSWORD;

    static final int INITIAL_CAPACITY = 5;
    ConcurrentLinkedQueue<Connection> pool = new ConcurrentLinkedQueue<>();

    private DBHandler() {
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

    public synchronized Connection getConnetion() throws SQLException {
        if (pool.isEmpty())
            pool.add(DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD));
        
        return pool.poll();
    }

    public synchronized void returnConnection(Connection connection){
        pool.add(connection);
    }
}
