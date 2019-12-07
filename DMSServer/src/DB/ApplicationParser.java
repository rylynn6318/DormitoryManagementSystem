package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.TreeSet;

import enums.Grade;
import models.Application;
import models.Score;

public class ApplicationParser {
	public static Boolean isExist(String studentID) throws ClassNotFoundException, SQLException
	{
		String sql = "SELECT 학번 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = "+ studentID;
		Connection connection = DBHandler.INSTANCE.getConnetion();
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

	public static int getSemester() throws ClassNotFoundException, SQLException 
	{
		String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 학기 = (SELECT max(학기) FROM " + DBHandler.DB_NAME + ".신청)";
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		rs.next();
		int result = rs.getInt("학기");
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return result;
	}
	
	public static int getNumOfLeftSeat(String dormName, int semester) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		Statement state2 = DBinfo.connection();
		
		String pureSemester = String.valueOf(semester).substring(4);
		String getNumOfPassedAppsQuery;
		String getCapacityQuery;
		ResultSet passed;
		ResultSet capacity;
		if(pureSemester.equals("01") || pureSemester.equals("04") || pureSemester.equals("02") || pureSemester.equals("05"))
		{
			getNumOfPassedAppsQuery = "SELECT COUNT(*) FROM (SELECT * FROM" + DBHandler.DB_NAME + ".배정내역 WHERE 생활관명=" + dormName + " AND 학기=" + semester + ")";
			getCapacityQuery = "SELECT 수용인원 FROM 생활관정보 WHERE 생활관명=" + dormName + "AND 학기=" + semester;
		}
		else
		{
			getNumOfPassedAppsQuery = "SELECT COUNT(*) FROM (SELECT * FROM " + DBHandler.DB_NAME + ".배정내역 WHERE 생활관명=" + dormName + " AND 학기=" + (semester - 1) + ")";
			getCapacityQuery = "SELECT 수용인원 FROM 생활관정보 WHERE 생활관명=" + dormName + "AND 학기=" + (semester - 1);
		}
		passed = state.executeQuery(getNumOfPassedAppsQuery);
		capacity = state2.executeQuery(getCapacityQuery);
		
		capacity.next();
		passed.next();
		
		return capacity.getInt("수용인원") - passed.getInt("COUNT(*)");
	}

	public static ArrayList<Application> getSortedApps(String dormName, int choice, int semester) throws ClassNotFoundException, SQLException 
	{
		Statement state = DBinfo.connection();
		
		ArrayList<Application> sortedApps = new ArrayList<Application>();
		
		String getUnsortedAppsQuery = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 생활관명=" + dormName + " AND 지망=" + choice + "학기=201901 AND 합격여부=N";
		ResultSet apps = state.executeQuery(getUnsortedAppsQuery);
		
		while(apps.next())
		{
			Application temp = new Application(apps.getString("학번"), apps.getString("생활관정보_생활관명"), apps.getString("생활관정보_성별"), apps.getInt("생활관정보_학기"), apps.getInt("지망"), 0);
			sortedApps.add(temp);
		}
		
		return sortedApps;
	}
	
	public static ArrayList<Score> getScores(String studentId, int twoSemesterBefore, int lastSemester) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		
		String getScoresQuery = "SELECT 학점,등급 FROM " + DBHandler.DB_NAME + ".점수 WHERE 학번=" + studentId + "AND 학기 BETWEEN '" + twoSemesterBefore + "' AND '" + lastSemester + "'";	//직전 2학기 점수 테이블 가져오는 쿼리
		ResultSet scores = state.executeQuery(getScoresQuery);
		
		ArrayList<Score> score = new ArrayList<Score>();
		while(scores.next())
		{	
			score.add(new Score(null, null, 0, scores.getInt("학점"), Grade.get(scores.getString("등급"))));
		}
		
		return score;
	}
	
	public static String getZipCode(String studentId) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		
		String zipCodeQuery = "SELECT 보호자우편번호 FROM " + DBHandler.DB_NAME + ".학생 WHERE 학번=" + studentId;
		ResultSet zipCode = state.executeQuery(zipCodeQuery);
		zipCode.next();
		
		return zipCode.getString("보호자우편번호");
	}

	public static void updatePasser(Application temp) 
	{
		// TODO Auto-generated method stub
		
	}
	
}
