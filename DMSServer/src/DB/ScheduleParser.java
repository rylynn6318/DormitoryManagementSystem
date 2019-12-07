package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.Code1.Page;

public class ScheduleParser
{
	//스케쥴을 확인하고 들어갈 수 있는 날짜인지 조회
	public static boolean isAdmissible(Page page) throws ClassNotFoundException, SQLException
	{
		// 지금 DB가 datetime 타입이 아니라 시분초설정이 오전9시로 고정되어있음 ㅇㅇ 그거 수정하고 다시 검사필요
		Date day = new Date(119,0,1,8,59,59); //2019-01-01
		Date start = null;
		Date end = null;

		// String.valueOf((int)page)를 String.valueOf((int)page.getCode())로 수정함. by ssm
		String sql = "SELECT 시작일, 종료일 FROM " + DBHandler.INSTANCE.DB_NAME + ".스케쥴  WHERE (`스케쥴 할일 코드_ID` =" + String.valueOf((int)page.getCode()) + ")";
		ResultSet resultSet = DBHandler.INSTANCE.excuteSelect(sql);
		resultSet.next();

		start = resultSet.getDate("시작일");
		end = resultSet.getDate("종료일");
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String time1 = format1.format(start);		
		System.out.println(time1);
		int result1 = day.compareTo(start);
		int result2 = day.compareTo(end);
		
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
}