package LoginChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class loginChecker {
	static final String DRIVER_NAME = "mysql";
	static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	static final String PORT = "3306";
	static final String DB_NAME = "Prototype";													//DB이름
	static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = "jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	public static int main(String id, String pw) throws ClassNotFoundException, SQLException
	{
		int result = 0;
		Connection conn = null;
		Statement state = null;
			
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		String sql = "SELECT PW, 계정 타입_코드 FROM " + DB_NAME + ".계정 WHERE ID = "+ id;
		ResultSet loginRs = state.executeQuery(sql);
		if(loginRs.next())
			{
				if(loginRs.getString("PW").equals(pw))
				{
					switch(loginRs.getInt("계정 타입_코드"))
					{
					case 1:
					{
						result = 1;		// 학생 로그인 성공
					}
					case 2:
					{
						result = 2;		// 선생님 로그인 성공
					}
					case 3:
					{
						result = 3;		// 관리자 로그인 성공
					}
					}
				}
				else
				{
					result = 4;			// 비밀번호 오류
				}
			}
			else
			{
				result = 5;				// 없는 아이디
			}
			return result;
	}
}
