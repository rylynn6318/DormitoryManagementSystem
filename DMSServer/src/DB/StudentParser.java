package DB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import shared.enums.Gender;

public class StudentParser {
	//3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
	public static Gender getGender(String id) throws Exception
	{
		Connection conn = null;
		Statement state = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DBinfo.DB_URL, DBinfo.USER_NAME, DBinfo.PASSWORD);		
		state = conn.createStatement();	
		String sql = "SELECT 성별 FROM " + DBinfo.DB_NAME + ".학생 WHERE 학번 = "+id;
		ResultSet purs = state.executeQuery(sql);
		
		return purs.getString("성별").equals("M") ? Gender.M : Gender.F;
		
	}
}
