package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	public static ArrayList<PlacementHistory> getAllResidence() throws SQLException, ClassNotFoundException
	{
		ArrayList<PlacementHistory> phArray = new ArrayList<PlacementHistory>();
		
		String sql = "SELECT * FROM" + DBHandler.DB_NAME + ".배정내역 WHERE 호실정보_학기=" + CurrentSemesterParser.getCurrentSemester();
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet ph = state.executeQuery();
		
		while(ph.next())
		{
			//혹시 나중에 sql.date를 util.date로 변환하는데 문제가 있으면 java.util.Date newDate = result.getTimestamp("VALUEDATE");를 사용해보시오.
			java.util.Date checkOut = new java.util.Date(ph.getDate("퇴사예정일").getTime());
			PlacementHistory temp = new PlacementHistory(ph.getString("학번"), ph.getInt("호실정보_호"), ph.getInt("호실정보_학기"), ph.getString("호실정보_생활관명"), Seat.get(ph.getString("자리")), checkOut);
			phArray.add(temp);
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return phArray;
	}
}