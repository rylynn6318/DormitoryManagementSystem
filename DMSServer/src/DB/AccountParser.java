package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.DBinfo;
import enums.Code2;

public class AccountParser {
	public static Code2.LoginResult check(String id, String pw) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT PW, 계정 타입_코드 FROM " + DBinfo.DB_NAME + ".계정 WHERE ID = "+ id;			//받아온 id pw를 db에서 조회
		ResultSet loginRs = state.executeQuery(sql);
		
		if(!loginRs.next())								//조회된게 없음.
		{
			return Code2.LoginResult.FAIL;
		}
		
		if(!loginRs.getString("PW").equals(pw))			//비밀번호가 불일치하면
		{
			return Code2.LoginResult.FAIL;
		}
		
		switch(loginRs.getInt("계정 타입_코드"))
		{
		case 1:																		
			return Code2.LoginResult.STUDENT;			//학생 성공
			
		case 2:
			return Code2.LoginResult.ADMIN;				//관리자 성공
			
		default:
			return Code2.LoginResult.FAIL;				//알수없음
		}
	}
}