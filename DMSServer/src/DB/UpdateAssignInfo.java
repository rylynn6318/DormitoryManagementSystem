package DB;

import java.sql.SQLException;
import java.sql.Statement;

public class UpdateAssignInfo 
{
	public static void updateAssignInfo (String studentID, String seat, int checkOut, int semesterCode, String roomNumber) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "INSERT INTO " + DBinfo.DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + studentID + "', '" + seat+ "', '" + checkOut + "', '오름1', '" + semesterCode + "', '" + roomNumber +"')";
		state.executeUpdate(sql);
	}
}
