package DB;

import java.sql.ResultSet;
import java.sql.Statement;
import enums.Gender;

public class StudentParser {
	//3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
	public static Gender getGender(String id) throws Exception
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT 성별 FROM " + DBinfo.DB_NAME + ".학생 WHERE 학번 = "+id;
		ResultSet purs = state.executeQuery(sql);
		
		return Gender.get(purs.getString("성별").charAt(0)); //.equals("M") ? Gender.Male : Gender.Female;
	}
}
