package logic;

import DB.ApplicationParser;
import enums.Grade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import models.*;
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
	
//	탑층 알고리즘 설계중
//	1. 탑층 capacity보다 신청이 많은지 확인
//	2. 일반 capacity보다 신청이 많은지 확인
//	
//	둘 다 true일 경우 -> 그대로 진행
//	1은 true, 2는 false일 경우 -> ???
//	1은 false, 2는 true일 경우 -> 1의 남은 capacity를 2에 합쳐서 선발
//	둘 다 false일 경우 -> 그대로 진행
		
	public static void selectionByChoice() throws ClassNotFoundException, SQLException
	{
		passerSelection("푸름1", 0);
		passerSelection("푸름1_탑층", 0);
		passerSelection("푸름2", 0);
		passerSelection("푸름2_탑층", 0);
		passerSelection("푸름3", 0);
		for(int choice = 1; choice < 4; choice++)
		{
			passerSelection("푸름1", choice);
			passerSelection("푸름1_탑층", choice);
			passerSelection("푸름2", choice);
			passerSelection("푸름2_탑층", choice);
			passerSelection("푸름3", choice);
			passerSelection("푸름4", choice);
			passerSelection("오름1", choice);
			passerSelection("오름2", choice);
			passerSelection("오름3", choice);
			passerSelection("신평_남", choice);
			passerSelection("신평_여", choice);
		}
	}
	
	public static void passerSelection(String dormName, int choice) throws SQLException, ClassNotFoundException
	{
		int semester = ApplicationParser.getSemester();
		
		int leftCapacity = ApplicationParser.getNumOfLeftSeat(dormName, semester);
		ArrayList<Application> apps = ApplicationParser.getSortedApps(dormName, choice, semester);
		
		Iterator<Application> iterator = apps.iterator();
		
		for(int i = 0; i < leftCapacity; i++)
		{
			Application temp = iterator.next();
			ApplicationParser.updatePasser(temp);
		}
	}
	
	public static double getFinalScore(String studentId, int semester) throws ClassNotFoundException, SQLException
	{	
		ArrayList<Score> score = ApplicationParser.getScores(studentId, pastTwo(semester), pastOne(semester));

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
	
}