package DB;

import java.sql.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum DBHandler {
    INSTANCE;

    static final String DRIVER_NAME = "mysql";
    static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
    static final String PORT = "3306";
    static final String DB_NAME = "Prototype";													//DB이름
    static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
    static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
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

    private synchronized Connection getConnetion() throws SQLException {
        if (pool.isEmpty())
            pool.add(DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD));
        
        return pool.poll();
    }

    private synchronized void returnConnection(Connection connection){
        pool.add(connection);
    }

    // select 할때 사용
    public ResultSet excuteSelect(String query) throws SQLException {
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(query);
        ResultSet result = state.executeQuery();
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);

        return result;
    }

    // Update, Delete, Insert 할때 사용
    public int excuteQuery(String query) throws SQLException {
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(query);
        int result = state.executeUpdate();
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);

        return result;
    }
}
