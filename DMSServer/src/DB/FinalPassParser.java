package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FinalPassParser 
{
	public static ResultSet getFinalPass() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT ID, 생활관명, 지망, 학기 FROM " + DBinfo.DB_NAME + ".신청 WHERE 최종결과 = 'Y' order by 생활관명, 코골이여부";  // 최종결과가 Y인 신청에 대해 정보를 가져옴
		ResultSet rurs = state.executeQuery(sql);
		return rurs;
	}
}
