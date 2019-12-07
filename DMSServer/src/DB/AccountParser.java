package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import enums.Code2;

public class AccountParser {
    public static Code2.LoginResult check(String id, String pw) throws ClassNotFoundException, SQLException {
		// 받아온 id pw를 db에서 조회
        String sql = "SELECT PW, 계정 타입_코드 FROM " + DBHandler.INSTANCE.DB_NAME + ".계정 WHERE ID = " + id;
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(sql);
        ResultSet loginRs = state.executeQuery();

        if (!loginRs.next()) // 조회된게 없음.
        {
            state.close();
            DBHandler.INSTANCE.returnConnection(connection);
            return Code2.LoginResult.FAIL;
        }
        if (!loginRs.getString("PW").equals(pw)) // 비밀번호가 불일치하면
        {
            state.close();
            DBHandler.INSTANCE.returnConnection(connection);
            return Code2.LoginResult.FAIL;
        }
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
        return Code2.LoginResult.get((byte) loginRs.getInt("계정 타입_코드"));
    }
}