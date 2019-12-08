package DB;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.Code1.Page;
import models.Schedule;

public class ScheduleParser
{
	//스케쥴을 확인하고 들어갈 수 있는 날짜인지 조회
	public static boolean isAdmissible(Page page) throws ClassNotFoundException, SQLException
	{
		// 지금 DB가 datetime 타입이 아니라 시분초설정이 오전9시로 고정되어있음 ㅇㅇ 그거 수정하고 다시 검사필요
		Date day = new Date(); //현재시간
		Date start = null;
		Date end = null;

		// String.valueOf((int)page)를 String.valueOf((int)page.getCode())로 수정함. by ssm
		String sql = "SELECT 시작일, 종료일 FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE (`스케쥴 할일 코드_ID` =" + String.valueOf((int)page.getCode()) + ")";
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();
		resultSet.next();

		start = resultSet.getDate("시작일");
		end = resultSet.getDate("종료일");
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String time1 = format1.format(start);		
		System.out.println(time1);
		int result1 = day.compareTo(start);
		int result2 = day.compareTo(end);

		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		if(result1 == -1)//시작일 보다 빠르면
		{
			System.out.println("해당 기간 아님");
			return false;
			
		}
		else if(result2 == 1) //종료일 보다 느리면
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
	

	public static String getDescription(Page page) throws Exception 
	{
		//5. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
		String sql = "SELECT 비고 FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE (`스케쥴 할일 코드_ID` =" + String.valueOf(page.getCode()) + ")";
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();
		resultSet.next();
		String result;
		result = resultSet.getString("비고");
		resultSet.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return result;
	}
	
	public static Schedule getSchedule(Page page) throws Exception
	{
		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE (`스케쥴 할일 코드_ID` =" + String.valueOf(page.getCode()) + ")";
		
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement state = connection.prepareStatement(sql);
		
		ResultSet resultSet = state.executeQuery(sql);
		resultSet.next();
		Schedule schedule = new Schedule(resultSet.getString("스케줄명") , resultSet.getInt("스케쥴 할일 코드_ID"), resultSet.getDate("시작일"), resultSet.getDate("종료일"), resultSet.getString("비고"));
		return schedule;
		
	}
}