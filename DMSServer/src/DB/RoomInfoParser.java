package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomInfoParser 
{
	public static ResultSet getRoomInfo() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT 호, 생활관명, 학기 FROM " + DBinfo.DB_NAME + ".호실정보";
		ResultSet rurs = state.executeQuery(sql);
		return rurs;
	}
}
