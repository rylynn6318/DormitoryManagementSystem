package assign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pass 
{
	public static void passUpdate() throws SQLException, ClassNotFoundException
	{	
		final String DRIVER_NAME = "mysql";
		final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
		final String PORT = "3306";
		final String DB_NAME = "Prototype";													//DB이름
		final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
		final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 									//사용자의 비밀번호를 상수로 정의
		final String DB_URL = 
						"jdbc:" + 
						DRIVER_NAME + "://" + 
						HOSTNAME + ":" + 
						PORT + "/" + 
						DB_NAME + "?user=" + 
						USER_NAME + "&password=" + 
						PASSWORD; 
		
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		Statement state2 = conn.createStatement();
		String sql = "SELECT * FROM " + DB_NAME + ".신청";
		ResultSet rs = state.executeQuery(sql);		
		while(rs.next())
		{
			if(rs.getString("납부여부").equals("Y") && rs.getString("합격여부").equals("Y"))
			{
				String query = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + rs.getInt("ID")+")";   // 납부여부 Y 합격여부 Y면 해당 ID의 최종결과 Y
				state2.executeUpdate(query);
			}
		}
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		passUpdate();
	}
}
