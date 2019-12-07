package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PassInformationParser {
	public static ResultSet getPassInformation() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT ID, 납부여부, 합격여부, 최종결과 FROM " + DBinfo.DB_NAME + ".신청";
		ResultSet purs = state.executeQuery(sql);
		return purs;
	}
}
