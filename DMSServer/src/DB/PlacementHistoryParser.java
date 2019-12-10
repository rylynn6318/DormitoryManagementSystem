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
		String sql = "SELECT 호실정보_호, 자리 FROM " + DBHandler.DB_NAME + ".배정내역 WHERE 학생_학번 = '"+ id + "' AND 호실정보_학기 = '" + CurrentSemesterParser.getCurrentSemester() + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		PlacementHistory history = null;
		String roomId = null;
		String seat = null;
		
		if(rs.next())
		{
			roomId = rs.getString("호실정보_호");
			seat = rs.getString("자리");
		}
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		try
		{
			history = new PlacementHistory(roomId, Seat.get(seat));
		}
		catch(Exception e)
		{
			System.out.println("PlacementHistory 생성 도중 예외 발생.");
			e.printStackTrace();
		}
		return history;
	}
	
	public static void insertPlacementHistory(PlacementHistory ph) throws SQLException
	{
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(ph.checkout.getYear(), ph.checkout.getMonth(), ph.checkout.getDay());
		
		String sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역(학생_학번, 호실정보_호, 호실정보_학기, 호실정보_생활관명, 자리, 퇴사예정일) VALUES ('" + ph.studentId + "', '" + ph.roomId + "', '" + ph.semester + "', '" + ph.dormitoryName + "', '" + ph.seat.abcd + "', '" + date + "')";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		state.execute();
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static void deletePlacamentHistory(String studentId) throws ClassNotFoundException, SQLException
	{
		String sql = "DELETE FROM " + DBHandler.DB_NAME + ".배정내역 WHERE 학생_학번 = '" + studentId + "' AND 호실정보_학기 = '" + CurrentSemesterParser.getCurrentSemester() + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		state.execute();
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static ArrayList<PlacementHistory> getAllResidence() throws SQLException, ClassNotFoundException
	{
		ArrayList<PlacementHistory> phArray = new ArrayList<PlacementHistory>();
		
		String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".배정내역 WHERE 호실정보_학기 = '" + CurrentSemesterParser.getCurrentSemester() + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet ph = state.executeQuery();
		
		while(ph.next())
		{
			//혹시 나중에 sql.date를 utils.date로 변환하는데 문제가 있으면 java.utils.Date newDate = result.getTimestamp("VALUEDATE");를 사용해보시오.
			java.util.Date checkOut = new java.util.Date(ph.getDate("퇴사예정일").getTime());
			PlacementHistory temp = new PlacementHistory(ph.getString("학생_학번"), ph.getString("호실정보_호"), ph.getInt("호실정보_학기"), ph.getString("호실정보_생활관명"), Seat.get(ph.getString("자리")), checkOut);
			phArray.add(temp);
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return phArray;
	}
	
	public static Boolean isExistPlcementHistory(String studentID) throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT `학생_학번` FROM "+ DBHandler.DB_NAME + ".배정내역 WHERE `학생_학번` = '"+ studentID + "' and `호실정보_학기` = '" + CurrentSemesterParser.getCurrentSemester() + "'";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		boolean isNext = rs.next();
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		if(isNext)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean isExistPlcementHistory(String studentId, int semester) throws SQLException {
		String sql = "SELECT `학생_학번` FROM "+ DBHandler.DB_NAME + ".배정내역 WHERE `학생_학번` = '"+ studentId + "' and `호실정보_학기` = '" + semester+ "'";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		boolean isNext = rs.next();
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		if(isNext)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}