package DB;

import java.sql.*;
import java.util.Iterator;
import java.util.TreeSet;

import enums.Grade;
import models.Application;
import models.Score;
import sun.security.x509.DNSName;

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
		String pureSemester = String.valueOf(semester).substring(4);
		String getNumOfPassedAppsQuery;
		String getCapacityQuery;
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

		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement passedState = connection.prepareStatement(getNumOfPassedAppsQuery);
		PreparedStatement capacityState = connection.prepareStatement(getCapacityQuery);

		ResultSet passed = passedState.executeQuery();
		ResultSet capacity = capacityState.executeQuery();
		
		capacity.next();
		passed.next();

		int result = capacity.getInt("수용인원") - passed.getInt("COUNT(*)");

		passedState.close();
		capacityState.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return result;
	}

	public static TreeSet<Application> getSortedApps(String dormName, int choice, int semester) throws ClassNotFoundException, SQLException 
	{
		TreeSet<Application> sortedApps = new TreeSet<Application>();
		
		String getUnsortedAppsQuery = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 생활관명=" + dormName + " AND 지망=" + choice + "학기=201901 AND 합격여부=N";
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement preparedStatement = connection.prepareStatement(getUnsortedAppsQuery);
		ResultSet apps = preparedStatement.executeQuery();
		
		while(apps.next())
		{
			Application temp = new Application(apps.getString("학번"), apps.getString("생활관정보_생활관명"), apps.getString("생활관정보_성별"), apps.getInt("생활관정보_학기"), apps.getInt("지망"), getFinalScore(apps.getString("학번"), apps.getInt("학기")));
			sortedApps.add(temp);
		}

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return sortedApps;
	}
	
//	여기서부터--------------------------------------------------------
	public static double getFinalScore(String studentId, int semester) throws ClassNotFoundException, SQLException
	{	
		TreeSet<Score> score = ApplicationParser.getScores(studentId, pastTwo(semester), pastOne(semester));

		double sumOfTakenGrade = 0;
		double sumOfTakenCredit = 0;
		
		Iterator<Score> scores = score.iterator();
		while(scores.hasNext())
		{
			Score temp = scores.next();
			sumOfTakenCredit += temp.credit;
			
			switch(temp.grade) 
			{
			case APLUS:
				sumOfTakenGrade += 4.5 * temp.credit;
				break;
			case A:
				sumOfTakenGrade += 4 * temp.credit;
				break;
			case BPLUS:
				sumOfTakenGrade += 3.5 * temp.credit;
				break;
			case B:
				sumOfTakenGrade += 3 * temp.credit;
				break;
			case CPLUS:
				sumOfTakenGrade += 2.5 * temp.credit;
				break;
			case C:
				sumOfTakenGrade += 2 * temp.credit;
				break;
			case DPLUS:
				sumOfTakenGrade += 1.5 * temp.credit;
				break;
			case D:
				sumOfTakenGrade += 1 * temp.credit;
				break;
			case F:
				break;
			}
		}
		
		return (sumOfTakenGrade/sumOfTakenCredit + getDistanceScore(ApplicationParser.getZipCode(studentId)));
	}
//	여기까지--------------------------------------------------------는 getSortedApps를 위한 로직임
	
	public static double getDistanceScore(String s)
	{
		int a = Integer.parseInt(s);		
		if(a/100 == 402) return 0.4;	//울릉도
		
		a = a/1000;
		if(a==63) return 0.4;	//제주도
		else if(35 <a && a<44) return 0.1;	//경북, 대구
		else if(43 <a && a<54) return 0.2;	//울산, 부산, 경남
		else if(33 <a && a<36) return 0.2;	//대전
		else
			return 0.3;
	}

	
	public static int pastOne(int semester)
	{
		String pureSemester = String.valueOf(semester).substring(4);	//학기부분만 잘라냄 ex)201901에서 01
		
		switch(pureSemester)
		{
		case "01":				//1학기
			semester -= 97;
			break;
		case "02":				//여름 계절
			semester -= 98;
			break;
		case "03":				//여름 계절 이후
			semester -= 99;
			break;
		case "04":				//2학기
			semester -= 3;
			break;
		case "05":				//겨울 계절
			semester -= 4;
			break;
		case "06":				//겨울 계절 이후
			semester -= 5;
			break;
		}
		
		return semester;
	}
	
	public static int pastTwo(int semester)
	{
		String pureSemester = String.valueOf(semester).substring(4);	//학기부분만 잘라냄 ex)201901에서 01
		
		switch(pureSemester)
		{
		case "01":
		case "04":
			semester -= 100;
			break;
		case "02":
		case "05":
			semester -= 101;
			break;
		case "03":
		case "06":
			semester -= 102;
			break;
		}
		
		return semester;
	}
	
	public static TreeSet<Score> getScores(String studentId, int twoSemesterBefore, int lastSemester) throws ClassNotFoundException, SQLException
	{
		String getScoresQuery = "SELECT 학점,등급 FROM " + DBHandler.DB_NAME + ".점수 WHERE 학번=" + studentId + "AND 학기 BETWEEN '" + twoSemesterBefore + "' AND '" + lastSemester + "'";	//직전 2학기 점수 테이블 가져오는 쿼리
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement preparedStatement = connection.prepareStatement(getScoresQuery);
		ResultSet scores = preparedStatement.executeQuery();

		TreeSet<Score> score = new TreeSet<Score>();
		while(scores.next())
		{	
			score.add(new Score(null, null, 0, scores.getInt("학점"), Grade.get(scores.getString("등급"))));
		}

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return score;
	}
	
	public static String getZipCode(String studentId) throws ClassNotFoundException, SQLException
	{
		String zipCodeQuery = "SELECT 보호자우편번호 FROM " + DBHandler.DB_NAME + ".학생 WHERE 학번=" + studentId;
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement preparedStatement = connection.prepareStatement(zipCodeQuery);
		ResultSet zipCode = preparedStatement.executeQuery();

		zipCode.next();

		String zipcode = zipCode.getString("보호자우편번호");

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return zipcode;
	}

	public static void updatePasser(Application temp) throws ClassNotFoundException, SQLException 
	{
		String setPassed = "UPDATE " + DBHandler.DB_NAME + ".신청 SET 합격여부=Y WHERE 학번=" + temp.getStudentId() + "지망=" + temp.getChoice() + "학기=" + temp.getSemesterCode();
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement preparedStatement = connection.prepareStatement(setPassed);
		ResultSet scores = preparedStatement.executeQuery();
		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
}
