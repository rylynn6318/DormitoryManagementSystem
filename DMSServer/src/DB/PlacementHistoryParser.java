package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import enums.Bool;
import enums.Seat;
import models.Application;
import models.PlacementHistory;

public class PlacementHistoryParser 
{
	public static PlacementHistory getPlacementResult(String id) throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT 호실정보_호, 자리 FROM" + DBHandler.DB_NAME + ".배정내역 WHERE 학번 = "+ id + "호실정보_학기 = " + CurrentSemesterParser.getCurrentSemester();
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		rs.next();
		PlacementHistory ph = new PlacementHistory(rs.getInt("호실정보_호"), Seat.get(rs.getString("자리")));
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return ph;
	}
}