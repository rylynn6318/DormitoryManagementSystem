package DB;

import java.sql.ResultSet;

import enums.Gender;

public class StudentParser {
    //3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
    public static Gender getGender(String id) throws Exception {
        String sql = "SELECT 성별 FROM " + DBHandler.INSTANCE.DB_NAME + ".학생 WHERE 학번 = " + id;
        ResultSet resultSet = DBHandler.INSTANCE.excuteSelect(sql);

        return Gender.get(resultSet.getString("성별")); //.equals("M") ? Gender.Male : Gender.Female;
    }
}