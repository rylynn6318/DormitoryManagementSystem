package logic;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DB.DBinfo;

public class loginChecker {
	public static int check(String id, String pw) throws ClassNotFoundException, SQLException
	{
		int result = 0;
		Statement state = DBinfo.connection();
		String sql = "SELECT PW, 계정 타입_코드 FROM " + DBinfo.DB_NAME + ".계정 WHERE ID = "+ id;			//받아온 id pw를 db에서 조회
		ResultSet loginRs = state.executeQuery(sql);
		if(loginRs.next())																		//조회된게 있으면
			{
				if(loginRs.getString("PW").equals(pw))											//비밀번호가 일치하면
				{
					switch(loginRs.getInt("계정 타입_코드"))
					{
					case 1:																		
					{
						result = 1;																// 학생 로그인 성공
					}
					case 2:
					{
						result = 2;																// 선생님 로그인 성공
					}
					case 3:
					{
						result = 3;																// 관리자 로그인 성공
					}
					}
				}
				else
				{
					result = 4;																	// 비밀번호 오류
				}
			}
			else
			{
				result = 5;																		// 없는 아이디
			}
			return result;
	}
}
