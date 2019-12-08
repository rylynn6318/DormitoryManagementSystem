package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import enums.Gender;

public class StudentParser {
    //3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
    public static Gender getGender(String id) throws Exception {
        String sql = "SELECT 성별 FROM " + DBHandler.INSTANCE.DB_NAME + ".학생 WHERE 학번 = " + id;
        Connection connection = DBHandler.INSTANCE.getConnection();
        PreparedStatement state = connection.prepareStatement(sql);
        ResultSet resultSet = state.executeQuery();

        Gender gender = Gender.get(resultSet.getString("성별"));

        state.close();
        DBHandler.INSTANCE.returnConnection(connection);

        return gender; //.equals("M") ? Gender.Male : Gender.Female;
    }
}