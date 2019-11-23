package assign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pass 
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
	
	public static void passUpdate() throws SQLException, ClassNotFoundException
	{	
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		Statement state2 = conn.createStatement();
		
		String sql = "SELECT ID, 납부여부, 합격여부, 최종결과 FROM " + DB_NAME + ".신청";
		ResultSet purs = state.executeQuery(sql);		
		while(purs.next())
		{
			if(purs.getString("납부여부").equals("Y") && purs.getString("합격여부").equals("Y"))
			{
				String query = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + purs.getInt("ID")+")";   // 납부여부 Y 합격여부 Y면 해당 ID의 최종결과 Y
				state2.executeUpdate(query);
			} 
		}

	}
	public static void residenceUpdate() throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String sql = "SELECT ID, 생활관명 FROM " + DB_NAME + ".신청 생활관명 WHERE (학기 = 201901 and 최종결과 = 'Y') order by 생활관명";   // 201901은 임시로 넣은것, 제대로 하려면 학기를 유동적으로 바꿀 수 있어야함.
		ResultSet rurs = state.executeQuery(sql);
		
		while(rurs.next())
		{
			System.out.println(rurs.getString("생활관명"));
		}
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		passUpdate();
		residenceUpdate();
	}
}
