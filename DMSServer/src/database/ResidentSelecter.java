package database;

import java.sql.Connection;
import java.sql.Statement;
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
	
	public void temp()	//실제 배치는 버튼 하나만 누르면 자동으로 다 되서 pass 함수만 가지고는 구현할 수 없음 -> pass함수를 모든 경우의 수로 돌려주는 함수가 필요함
	{
//		알고리즘
//		SQL문 SELECT * FROM 신청 WHERE 생활관명=오름3 && 지망=1 && 학기=201901 && 합격여부=N 를 통해 현재 학기에서 생활관명, 지망이 동일하고 합격여부가 Y인 것들만 뽑아낸 테이블과 생활관.getCapacity()를 pass 함수에 넣음
//		SELECT COUNT(*) FROM (SELECT * FROM 배정내역 WHERE 생활관명=찾을 생활관 명 AND 학기=현재학기)의 결과가 생활관.getCapacity()보다 작으면 생활관.getCapacity에서 앞 SQL문의 결과로 나온 값만큼 빼서 pass를 다시 돌림
//		SELECT COUNT(*) FROM (SELECT * FROM 배정내역 WHERE 생활관명=찾을 생활관 명 AND 학기=현재학기)의 결과가 생활관.getCapacity()과 같아질 때 까지 반복
//		위 3단계를 각 생활관별로 시행한다.
//		근데 생각해보니까 합격자 선발 알고리즘 돌리기 전에 Student에서 평균점수 뽑고 신청 테이블에서 가져온 Application배열의 각 객체 전부에서 평균점수 + 가산점을 계산해서 변수를 다 채운 다음에 돌려야하는데
//		그럼 신청 테이블을 어디에서 제일 먼저 가져오고 돌아다니면서 지역변수 채우는거 어떻게 해야할까?
	}
	
	public void selecter() throws SQLException, ClassNotFoundException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		TreeSet<Application> sortedApp = new TreeSet<Application>();
		int choice = 1;
		
		String query = "SELECT * FROM 신청 WHERE 생활관명=푸름1 && 지망=" + choice + "학기=201901 && 합격여부=N";   // 푸름 1을 choice지망으로 하고 학기가 201901이며 합격여부가 N인 신청
		ResultSet apps = state.executeQuery(query);
		
		while(apps.next())
		{
			Application temp = new Application(apps.getString("학번"), apps.getString("생활관정보_생활관명"), apps.getString("생활관정보_성별"), apps.getInt("생활관정보_학기"), apps.getInt("지망"));
			setFinalScore(temp);
			
			sortedApp.add(temp);
		}
	}

//	1. 학번을 가지고 점수 테이블로 감 v
//	2. 해당 학번의 직전 2학기치 점수를 다 들고옴 v
//	3. 평균 점수를 구함 v
//	4. 어딘가에서 구해온 가산점을 더함 x
//	5. Application에 넣음 v
//	6. 반환 v
	public void setFinalScore(Application a) throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String query = "SELECT 학점,등급 FROM 점수 WHERE 학번=" + a.getStudentId() + "학기 BETWEEN '" + pastTwo(a.getSemesterCode()) + "' AND '" + pastOne(a.getSemesterCode()) + "'";	//직전 2학기 점수 테이블 가져오는 쿼리
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
	
	public int pastOne(int semester)
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
	
	public int pastTwo(int semester)
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
	
	//SQL문 SELECT * FROM 신청 WHERE 생활관명=오름3 && 지망=1 && 학기=201901 이런 식으로 현재 학기에서 생활관명, 지망이 동일한 것들만 뽑아낸 테이블을 넣어줘야함
	public void pass(Application[] apps, int num)	//apps == 정렬되기 전의 신청 배열, num == 기숙사의 수용인원 - 현재 합격된 인원 
	{
		//로컬 변수명이 대문자로 시작하는거 꼬와서 SortedApp을 sortedApp으로 수정함.
		//대괄호 자바식으로 되어있던거 꼬와서 수정함.
		TreeSet<Application> sortedApp = new TreeSet<Application>();
		
		//TreeSet은 자동으로 정렬을 해주니까 등록을 전부 가져와서 때려박으면 성적으로 정렬해줌
		for(int i = 0; i < apps.length; i++) 
		{
			sortedApp.add(apps[i]);
		}
		
		Iterator<Application> iterator = sortedApp.iterator();	//sortedApp에 순차적 접근을 하기 위한 반복자
		
		for(int i = 0; i < num; i++)	// 신청의 isPassed를 true로 바꾸는 작업을 필요한 수만큼 시행
		{
			Application temp = iterator.next();
//			temp.setPassed(true);
//			UPDATE 신청 SET 합격여부=Y WHERE 학번=temp.getStudentId() && 지망=temp.getChoice && 학기=temp.getSemesterCode;
//			정렬을 하기 위해 테이블 가져와서 만든 객체는 딱히 통과여부를 true로 만들어줄 필요가 없는건가??? 일단 주석 처리 해둠
		}
	}
	
	
	public void deleteApplication(String studentID, String dormitoryName, int semesterCode, int choice)	//입사 선발자 조회 및 관리 페이지에서 삭제 버튼을 누르면 사용할 SQL
	{
//			DELETE FROM 신청 WHERE 학번=studentID && 생활관정보_기숙사명=dormitoryName && 생활관정보_학기=semesterCode && 지망=choice;
	}
	
}