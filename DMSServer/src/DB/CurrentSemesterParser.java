package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrentSemesterParser 
{
	public static int getCurrentSemester() throws ClassNotFoundException, SQLException
	{
		Statement state1 = DBinfo.connection();
		String sql = "SELECT 학기 FROM " + DBinfo.DB_NAME + ".신청 ORDER BY 학기 DESC LIMIT 1"; //신청테이블에서 하나만 가져와서 그 학기를 봄
		ResultSet rcrs = state1.executeQuery(sql);
		rcrs.next();
		int currentSemester = rcrs.getInt("학기");
		return currentSemester;
	}
}
