package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateFinalPassParser {
    public static void updateFinalPass(String studentID) throws ClassNotFoundException, SQLException {
        // 납부여부 Y 결핵 통과 Y 합격여부 Y면 해당 ID의 최종결과 Y
        String sql = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + studentID + ")";
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(sql);
        state.executeUpdate();
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
    }
}
