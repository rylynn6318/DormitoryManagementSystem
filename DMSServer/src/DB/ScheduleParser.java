package DB;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import enums.Code1.Page;
import models.Application;
import models.Schedule;
import models.ScheduleCode;

public class ScheduleParser
{
	//스케쥴을 확인하고 들어갈 수 있는 날짜인지 조회
	public static boolean isAdmissible(Page page) throws ClassNotFoundException, SQLException
	{
		// 지금 DB가 datetime 타입이 아니라 시분초설정이 오전9시로 고정되어있음 ㅇㅇ 그거 수정하고 다시 검사필요
		Date day = new Date(); //현재시간
		Timestamp start = null;
		Timestamp end = null;		
	
		// String.valueOf((int)page)를 String.valueOf((int)page.getCode())로 수정함. by ssm
		String sql = "SELECT 시작일, 종료일 FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE (`스케쥴 할일 코드_ID` ='" + String.valueOf((int)page.getCode()) + "')";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();
		
		if(resultSet.next())
		{
			start = resultSet.getTimestamp("시작일");
			end = resultSet.getTimestamp("종료일");		
		}
		
		//널값 반환해야되는게 맞는데;
		if(start == null || end == null)
			throw new SQLException("isAdmissible : end와 start가 조회된 결과가 없습니다.");
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String time1 = format1.format(start);			
		String time2 = format1.format(end);		
		String time3 = format1.format(day);
		System.out.println("현재시간: " + time3);
		System.out.println("시작일: "+time1);
		System.out.println("종료일: "+time2);
		
		int result1 = day.compareTo(start);
		int result2 = day.compareTo(end);

		//자원 반환
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
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();
		
		String result = null;
		if(resultSet.next())
		{
			result = resultSet.getString("비고");
		}
		
		//자원 반환
		resultSet.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return result;
	}
	
	//만들긴했는데 당장 쓰진 않지만 혹시 라도 필요하면 사용하세요,,, 스케줄객체 생성하는 함수입니다 ㅠㅡㅠ........-서희- <- 이거 java.sql.Date를 java.util.Date에 바로 집어넣으면 안됨 일단 주석처리 해두겠음
//	public static Schedule[] getAllSchedule() throws Exception
//	{
//		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME +";";
//		
//		Connection connection = DBHandler.INSTANCE.getConnection();
//		PreparedStatement state = connection.prepareStatement(sql);
//		
//		ResultSet resultSet = state.executeQuery(sql);
//		resultSet.last();
//		Schedule[] schedule = new Schedule[resultSet.getRow()];
//		resultSet.first();
//		int index = 0;
//		while(resultSet.next())
//		{
//			schedule[index] = new Schedule(resultSet.getString("스케줄명") , resultSet.getInt("스케쥴 할일 코드_ID"), resultSet.getDate("시작일"), resultSet.getDate("종료일"), resultSet.getString("비고"));
//			index++;
//		}
//		return schedule;
//	}
	
	public static ArrayList<Schedule> getAllSchedule() throws SQLException
	{
		ArrayList<Schedule> schedule = new ArrayList<Schedule>();
		String getAllScheduleSql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴";
		System.out.println(getAllScheduleSql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(getAllScheduleSql);
		ResultSet rsSchedule = state.executeQuery();
		
		while(rsSchedule.next())
		{
			java.sql.Date sqlStartDate = rsSchedule.getDate("시작일");
			java.sql.Date sqlEndDate = rsSchedule.getDate("종료일");
			
			@SuppressWarnings("deprecation")
			java.util.Date startDate = new java.util.Date(sqlStartDate.getYear(), sqlStartDate.getMonth(), sqlStartDate.getDay());
			@SuppressWarnings("deprecation")
			java.util.Date endDate = new java.util.Date(sqlEndDate.getYear(), sqlEndDate.getMonth(), sqlEndDate.getDay());
			
			Schedule temp = new Schedule(rsSchedule.getInt("ID"), rsSchedule.getInt("`스케쥴 할일 코드_ID`"), startDate, endDate, rsSchedule.getString("비고"));
			schedule.add(temp);
		}
		
		rsSchedule.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return schedule;
	}
	
	public static ArrayList<ScheduleCode> getScheduleCode() throws SQLException
	{
		//1. 스케쥴 할일 코드 테이블에서 '코드', '이름' 을 객체로 만들어 배열로 가져온다.
		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".`스케쥴 할일 코드`";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		ArrayList<ScheduleCode> s = new ArrayList<ScheduleCode>();
		
		while(rs.next())
		{
			ScheduleCode sc = new ScheduleCode(rs.getInt("코드"), rs.getString("이름"));
			s.add(sc);
		}
		
		rs.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return s;
	}
	
	public static boolean isExist(String scheduleId) throws SQLException
	{
		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE ID = '" + scheduleId + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();
		
		boolean isExist = false;
		if(resultSet.next())
		{
			isExist = true;
		}
		
		//자원 반환
		resultSet.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return isExist;
	}
	
	public static boolean deleteSchedule(String scheduleId) throws SQLException
	{
		String sql = "DELETE FROM " + DBHandler.DB_NAME + ".스케쥴 WHERE ID ='" + scheduleId + "'";
		
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		
		boolean isSucceed = false;
		
		try
		{
			preparedStatement.executeUpdate(sql);
			isSucceed = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		DBHandler.INSTANCE.returnConnection(connection);
		
		return isSucceed;
	}
}