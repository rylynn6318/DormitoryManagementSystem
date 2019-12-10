package logic;

import DB.ApplicationParser;
import DB.CurrentSemesterParser;
import DB.DBHandler;

import java.sql.SQLException;
import java.util.ArrayList;
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
		ArrayList<String> dorms = ApplicationParser.getDormList();
		ArrayList<String> yearDorms = ApplicationParser.getYearDormList();
		
		for(String name : yearDorms)
			passerSelection(name, 0);
		
		for(int choice = 1; choice < 4; choice++)
			for(String name : dorms)
				passerSelection(name, choice);
	}
	
	public static void passerSelection(String dormName, int choice) //throws ClassNotFoundException, SQLException
	{
		int semester;
		try {
			semester = CurrentSemesterParser.getCurrentSemester();
		} catch (SQLException e) {
			System.out.println("학기 가져오는 도중 에러 발생");
			return;
		}
		
		int leftCapacity;
		try {
			leftCapacity = ApplicationParser.getNumOfLeftSeat(dormName, semester);
		} catch (SQLException e) {
			System.out.println(dormName + "의 남은 자리 수 가져오는 도중 에러 발생");
			return;
		}
		
		TreeSet<Application> apps;
		try {
			apps = ApplicationParser.getSortedApps(dormName, choice, semester);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(dormName + "의 정렬된" + choice + "지망 신청 가져오는 도중 에러 발생");
			return;
		}
		
		Iterator<Application> iterator = apps.iterator();
		if(apps.isEmpty())
		{
			System.out.println(dormName + "의 " + choice + "지망 신청이 존재하지 않거나 이미 다른 신청이 합격한 학생의 신청만 존재");
			return;
		}
		
		for(int i = 0; i < leftCapacity; i++)
		{
			if(iterator.hasNext())
			{
				Application temp = iterator.next();
				try {
					System.out.println(ApplicationParser.updatePasser(temp) + "개의 신청이 합격으로 처리됨");
				} catch (SQLException e) {
					System.out.println("합격여부를 Y로 업데이트 하는 도중 에러 발생");
					return;
				}
			}
			else
			{
				System.out.println("완료 - 생활관 수용인원보다 신청의 수가 더 적음");
				break;
			}
		}
		
		System.out.println(dormName + " " + choice + "지망 완료");
	}
	
}