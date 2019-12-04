package database;

import java.util.Iterator;
import java.util.TreeSet;
import shared.classes.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResidentSelecter 
{
	static final String DRIVER_NAME = "mysql";
	static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	static final String PORT = "3306";
	static final String DB_NAME = "Prototype";													//DB이름
	static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 									//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		realSelecter("푸름1");
		realSelecter("푸름2");
		realSelecter("푸름3");
		realSelecter("푸름4");
		realSelecter("오름1");
		realSelecter("오름2");
		realSelecter("오름3");
		realSelecter("신평_남");
		realSelecter("신평_여");
	}

	
	public static void realSelecter(String dormName) throws SQLException, ClassNotFoundException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		int leftCapacity = getNumOfLeftSeat(dormName);
		TreeSet<Application> apps = selecter(dormName);
		
		Iterator<Application> iterator = apps.iterator();
		
		for(int i = 0; i < leftCapacity; i++)
		{
			Application temp = iterator.next();
			String update = "UPDATE 신청 SET 합격여부=Y WHERE 학번=" + temp.getStudentId() + " AND 생활관정보_생활관명=" + temp.getDormitoryName() + " AND 성별=" + temp.getGender() + " AND 학기=" + temp.getSemesterCode() + " AND 지망=" + temp.getChoice();
			state.executeUpdate(update);
		}
	}
	
	public static TreeSet<Application> selecter(String dormName) throws SQLException, ClassNotFoundException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		TreeSet<Application> sortedApps = new TreeSet<Application>();
		int choice = 1;
		
		String query = "SELECT * FROM 신청 WHERE 생활관명=" + dormName + " AND 지망=" + choice + "학기=201901 AND 합격여부=N";   // 푸름 1을 choice지망으로 하고 학기가 201901이며 합격여부가 N인 신청
		ResultSet apps = state.executeQuery(query);
		
		while(apps.next())
		{
			Application temp = new Application(apps.getString("학번"), apps.getString("생활관정보_생활관명"), apps.getString("생활관정보_성별"), apps.getInt("생활관정보_학기"), apps.getInt("지망"));
			setFinalScore(temp);
			
			sortedApps.add(temp);
		}
		
		return sortedApps;
	}
	
	public static int getNumOfLeftSeat(String dormName) throws ClassNotFoundException, SQLException	//생활관 이름을 넣으면 남은 자리의 수를 리턴하는 함수
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String query = "SELECT COUNT(*) FROM (SELECT * FROM 배정내역 WHERE 생활관명=" + dormName + " AND 학기=" + 201901 + ")";
		ResultSet passed = state.executeQuery(query);
		
		int leftCapacity = 0;
		
		Statement state2 = conn.createStatement();
		query = "SELECT 수용인원 FROM 생활관정보 WHERE 생활관명=" + dormName + "AND 학기=" + 201901;
		ResultSet capacity = state2.executeQuery(query);
		
		capacity.next();
		passed.next();
		leftCapacity = capacity.getInt("수용인원") - passed.getInt("COUNT(*)");
		return leftCapacity;
	}
	

//	가산점 구해서 더하는거 만들어야됨
	public static void setFinalScore(Application a) throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String query = "SELECT 학점,등급 FROM 점수 WHERE 학번=" + a.getStudentId() + "AND 학기 BETWEEN '" + pastTwo(a.getSemesterCode()) + "' AND '" + pastOne(a.getSemesterCode()) + "'";	//직전 2학기 점수 테이블 가져오는 쿼리
		ResultSet scores = state.executeQuery(query);
		
		double sumOfTakenGrade = 0;
		double sumOfTakenCredit = 0;
		
		while(scores.next())
		{
			sumOfTakenCredit += scores.getInt("학점");
			switch(scores.getString("성적등급")) 
			{
			case "A+":
				sumOfTakenGrade += 4.5 * scores.getInt("학점");
				break;
			case "A":
				sumOfTakenGrade += 4 * scores.getInt("학점");
				break;
			case "B+":
				sumOfTakenGrade += 3.5 * scores.getInt("학점");
				break;
			case "B":
				sumOfTakenGrade += 3 * scores.getInt("학점");
				break;
			case "C+":
				sumOfTakenGrade += 2.5 * scores.getInt("학점");
				break;
			case "C":
				sumOfTakenGrade += 2 * scores.getInt("학점");
				break;
			case "D+":
				sumOfTakenGrade += 1.5 * scores.getInt("학점");
				break;
			case "D":
				sumOfTakenGrade += 1 * scores.getInt("학점");
				break;
			case "F":
				break;
			}
		}
		
		a.setScore(sumOfTakenGrade/sumOfTakenCredit);
	}
	
	public static int pastOne(int semester)
	{
		String pureSemester = String.valueOf(semester).substring(4);	//학기부분만 잘라냄 ex)201901에서 01
		
		switch(pureSemester)
		{
		case "01":
			semester -= 98;
			break;
		case "02":
			semester -= 99;
			break;
		case "03":
			semester -= 2;
			break;
		case "04":
			semester -= 3;
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
		case "03":
			semester -= 100;
			break;
		case "02":
		case "04":
			semester -= 101;
			break;
		}
		
		return semester;
	}
	
}