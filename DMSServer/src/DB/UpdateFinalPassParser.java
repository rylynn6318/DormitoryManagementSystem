package DB;

import java.sql.SQLException;
import java.sql.Statement;

public class UpdateFinalPassParser 
{
	public static void updateFinalPass(String studentID) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + studentID +")";   // 납부여부 Y 결핵 통과 Y 합격여부 Y면 해당 ID의 최종결과 Y 
		state.executeUpdate(sql);
	}
}
