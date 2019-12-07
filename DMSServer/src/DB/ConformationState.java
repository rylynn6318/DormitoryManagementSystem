package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConformationState 
{
	public static ResultSet getConformationState(String studentID) throws SQLException, ClassNotFoundException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT 확인여부 FROM " + DBinfo.DB_NAME + ".서류 WHERE 서류유형 = 1 and 진단일 BETWEEN '19/01/01' and '19/09/01 ' and 학생_ID = " + studentID;
		ResultSet purs = state.executeQuery(sql);
		return purs;
	}
}
