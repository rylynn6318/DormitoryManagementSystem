package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;

import models.Application;

public class ApplicationParser {
	public static Boolean isExist(String studentID) throws ClassNotFoundException, SQLException
	{
		String sql = "SELECT 학번 FROM " + DBHandler.INSTANCE.DB_NAME + ".신청 WHERE 학번 = "+ studentID;
		ResultSet rs = DBHandler.INSTANCE.excuteSelect(sql);
		
		if(rs.next())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static int getSemester() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static TreeSet<Application> getUnsortedApps(String dormName, int choice, int semester) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void updatePasser(Application temp) {
		// TODO Auto-generated method stub
		
	}
	
}
