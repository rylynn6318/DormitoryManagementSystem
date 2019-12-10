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
	
	public static ArrayList<Schedule> getAllSchedule() throws Exception
	{
		ArrayList<Schedule> schedule = new ArrayList<Schedule>();
		String getAllScheduleSql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴";
		System.out.println(getAllScheduleSql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(getAllScheduleSql);
		ResultSet rsSchedule = state.executeQuery();
		
		while(rsSchedule.next())
		{
			java.sql.Date sqlStartDate = null;
			java.sql.Date sqlEndDate = null;
			try
			{
				sqlStartDate = rsSchedule.getDate("시작일");
			}
			catch(Exception e)
			{
				throw new Exception("시작일 가져오는 중 오류 발생.");
			}
			
			try
			{
				sqlEndDate = rsSchedule.getDate("종료일");
			}
			catch(Exception e)
			{
				throw new Exception("종료일 가져오는 중 오류 발생.");
			}
			
			@SuppressWarnings("deprecation")
			java.util.Date startDate = sqlDateToUtilDate(sqlStartDate);
			@SuppressWarnings("deprecation")
			java.util.Date endDate = sqlDateToUtilDate(sqlEndDate);
			
			//현재 긁어오면 시간이 안긁어와짐; 누가 고쳐줘 (2019-12-10 명근)
			
			int scheduleId = rsSchedule.getInt("ID");
			int code = rsSchedule.getInt("스케쥴 할일 코드_ID");
			String description =  rsSchedule.getString("비고");
			
			Schedule temp = new Schedule(scheduleId, code, startDate, endDate, description);
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
		System.out.println(sql);
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
	
	//아이디로 스케쥴이 있는지 찾는다.
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
	
	//스케쥴 할일코드, 시작일, 종료일로 중복값이 있나 찾는다.
	public static boolean isExist(Schedule schedule) throws SQLException
	{
		int code = schedule.code;
		java.sql.Date statDate = utilDateToSqlDate(schedule.startDate);
		java.sql.Date endDate = utilDateToSqlDate(schedule.endDate);
		
		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = '" + String.valueOf(code) + 
				"' AND `시작일` = '" + statDate + "' AND 종료일 = '" + endDate + "'";
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
		System.out.println(sql);
		
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
	
	public static boolean insertSchedule(Schedule schedule) throws SQLException
	{
		int newId = getFinalId() + 1;
		String todoCode = String.valueOf(schedule.code);
		Date startDate = schedule.startDate;
		Date endDate = schedule.endDate;
		String description = schedule.description != null ? schedule.description : "" ;
		
		System.out.println(startDate.toString());
		System.out.println(endDate.toString());
		
		java.sql.Date sqlStartDate = utilDateToSqlDate(startDate);
		java.sql.Date sqlEndDate = utilDateToSqlDate(endDate);
		
		System.out.println(sqlStartDate);
		System.out.println(sqlEndDate);
		
		String sql = "INSERT INTO " + DBHandler.INSTANCE.DB_NAME+".스케쥴 VALUES('" + String.valueOf(newId) + "','" + todoCode + "','"+ sqlStartDate +
				"','"+ sqlEndDate + "','" + description + "')";
		System.out.println(sql);
		
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
	
	//스케쥴 테이블에 들어있는 항목의 총 개수
	public static int getFinalId() throws SQLException
	{
		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴";
		System.out.println(sql);
		
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();
		
		int count = 0;
		if(resultSet.next())
		{
			resultSet.last();
			count = resultSet.getInt("ID");
		}
		
		//자원 반환
		resultSet.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return count;
	}
	
	public static java.sql.Date utilDateToSqlDate(java.util.Date utilDate)
	{
		return new java.sql.Date(utilDate.getYear(), utilDate.getMonth(), utilDate.getDay()+8);
		
	}
	
	public static java.util.Date sqlDateToUtilDate(java.sql.Date utilDate)
	{
		return new java.util.Date(utilDate.getTime());
	}
	
	
}