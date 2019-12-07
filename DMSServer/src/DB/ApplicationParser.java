package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ApplicationParser {
	public static Boolean isExist(int studentID) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT 학번 FROM " + DBinfo.DB_NAME + ".신청 WHERE 학번 = "+ studentID;
		ResultSet rs = state.executeQuery(sql);
		
		if(rs.next())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
