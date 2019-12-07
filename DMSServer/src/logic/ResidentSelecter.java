package logic;

import DB.ApplicationParser;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeSet;
import models.*;

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
	
	public static void passerSelection(String dormName, int choice) throws ClassNotFoundException, SQLException
	{
		int semester = ApplicationParser.getSemester();
		
		int leftCapacity = ApplicationParser.getNumOfLeftSeat(dormName, semester);
		TreeSet<Application> apps = ApplicationParser.getSortedApps(dormName, choice, semester);
		
		Iterator<Application> iterator = apps.iterator();
		
		for(int i = 0; i < leftCapacity; i++)
		{
			Application temp = iterator.next();
			ApplicationParser.updatePasser(temp);
		}
	}
	
}