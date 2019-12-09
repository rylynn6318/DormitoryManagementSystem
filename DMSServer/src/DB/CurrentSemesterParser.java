package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentSemesterParser 
{
	@SuppressWarnings("deprecation")
	public static int getCurrentSemester() throws ClassNotFoundException, SQLException
	{
		Date time = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");					
		String time1 = format1.format(time);
		int result;	
		String sql1 = "SELECT `스케쥴 할일 코드_ID`, 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID`%'10' = '6' AND `시작일`>='" + (time.getYear()+1900) + "0101' AND `시작일`<='"+(time.getYear()+1900)+"1231'";
		String sql2 = "SELECT `스케쥴 할일 코드_ID`, 시작일, 종료일 , 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID`%'10' = '7' AND `시작일`>='" + (time.getYear()+1900) + "0101' AND `시작일`<='"+(time.getYear()+1900)+"1231'";
		String sql3 = "SELECT `스케쥴 할일 코드_ID`, 시작일, 종료일 , 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID`%'10' = '8' AND `시작일`>='" + (time.getYear()+1900) + "0101' AND `시작일`<='"+(time.getYear()+1900)+"1231'";
		String sql4 = "SELECT `스케쥴 할일 코드_ID`, 시작일, 종료일 , 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID`%'10' = '9' AND `시작일`>='" + (time.getYear()+1900) + "0101' AND `시작일`<='"+(time.getYear()+1900)+"1231'";
//		String sql = "SELECT `스케쥴 할일 코드`, 비고 FROM " + DBinfo.DB_NAME + ".스케쥴 <![CDATA[ WHERE 시작일 <="+time1+" and "+time1 + "<= 종료일   ]]>"; //만약 위 방법이 안되면 사용할것		 

		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state1 = connection.prepareStatement(sql1);
		ResultSet rs1 = state1.executeQuery();	
		rs1.next();		
		PreparedStatement state2 = connection.prepareStatement(sql2);
		ResultSet rs2 = state2.executeQuery();
		rs2.next();		
		PreparedStatement state3 = connection.prepareStatement(sql3);
		ResultSet rs3 = state3.executeQuery();
		rs3.next();		
		PreparedStatement state4 = connection.prepareStatement(sql4);
		ResultSet rs4 = state4.executeQuery();
		rs4.next();		
		
		
		if(rs1.getDate("시작일").before(time) && time.before(rs1.getDate("종료일")))
		{
			result = rs1.getInt("비고"); //1학기
			rs1.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return result;
		}
		else if(rs2.getDate("시작일").before(time)&& time.before(rs2.getDate("종료일"))) 
		{
			result = rs2.getInt("비고"); //여름계절
			rs2.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return result;
		}		
		else if(rs3.getDate("시작일").before(time)&& time.before(rs3.getDate("종료일"))) 
		{
			result = rs3.getInt("비고");//2학기
			rs3.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return result;
		}
		else if(rs4.getDate("시작일").before(time) && time.before(rs3.getDate("종료일")))
		{
			result = rs4.getInt("비고"); //겨울계절
			rs4.close();
			DBHandler.INSTANCE.returnConnection(connection);		
			return result;
		}
		else
		{
			System.out.println("CurrentSemesterParser 오류");
			return 0;
		}			
	}
//	public static void main(String args[]) throws ClassNotFoundException, SQLException	//테스트용 Main문
//	{
//		int a = getCurrentSemester();
//		System.out.println(a);
//	}
}
