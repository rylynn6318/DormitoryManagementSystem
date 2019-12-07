package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomBoarderParser 
{
	public static ResultSet getRoomBoarder() throws ClassNotFoundException, SQLException
	{
		String sql = "SELECT 호실정보_호, 자리, 학생_ID FROM " + DBinfo.DB_NAME + ".배정내역 WHERE 퇴사예정일 > "+ AvailablePeriodParser.getAvailablePeriod();// 여러 배정내역 (몇년 전꺼까지도) 중에서 아직 쓰고있는 방 예를들어 지금 2학기인데 1학기 1년 입사자
		//호 옆에 생활관명 넣어야함
		Statement state = DBinfo.connection();
		ResultSet rurs = state.executeQuery(sql);
		return rurs;
	}
}
