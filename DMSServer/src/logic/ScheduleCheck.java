package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.Code1.Page;

public class ScheduleCheck{
	static final String DRIVER_NAME = "mysql";
	static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	static final String PORT = "3306";
	static final String DB_NAME = "Prototype";													//DB이름
	static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	private static int currentSemester;
	
	public static boolean check(Page page) throws ClassNotFoundException, SQLException
	{
		// 지금 DB가 datetime 타입이 아니라 시분초설정이 오전9시로 고정되어있음 ㅇㅇ 그거 수정하고 다시 검사필요
		Date day = new Date(119,0,1,8,59,59); //2019-01-01
		Date start=null;
		Date end=null;
		
		Connection conn = null;
		Statement state = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();		

		// String.valueOf((int)page)를 String.valueOf((int)page.getCode())로 수정함. by ssm
		String sql = "SELECT 시작일, 종료일 FROM " + DB_NAME + ".스케쥴  WHERE (`스케쥴 할일 코드_ID` ="+String.valueOf((int)page.getCode()) +")";
		ResultSet purs = state.executeQuery(sql);
		purs.next();
		start=purs.getDate("시작일");
		end=purs.getDate("종료일");
		
		SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String time1 = format1.format(start);		
		System.out.println(time1);
		int result1=day.compareTo(start);
		int result2=day.compareTo(end);
		
		if(result1==-1)//시작일 보다 빠르면
		{
			System.out.println("해당 기간 아님");
			return false;
			
		}
		else if(result2==1) //종료일 보다 느리면
		{
			System.out.println("해당 기간 아님");
			return false;
		}
		else
		{
			System.out.println("해당 기간 맞음 ㅇㅇ");
			return true;
		}			
	}
}
