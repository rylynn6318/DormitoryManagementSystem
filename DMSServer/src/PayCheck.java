import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
public class PayCheck {
	static final String DRIVER_NAME = "mysql";
	static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	static final String PORT = "3306";
	static final String DB_NAME = "Prototype";													//DB이름
	static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	private static int currentSemester;
	
	public static void currentSemester() throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String sql = "SELECT 학기 FROM" + DB_NAME + ".신청 limit1"; //신청테이블에서 하나만 가져와서 그 학기를 봄
		ResultSet rcrs = state.executeQuery(sql);
		currentSemester = rcrs.getInt("학기");
	}
	
	public static void main(String[] args) throws IOException, Exception
	{
		currentSemester();
		
		File csv = new File("C:|Users|DongHyeon|Downloads");
		BufferedReader br = new BufferedReader(new FileReader(csv));
		String line ="";
		ArrayList<String> csvlist = new ArrayList<String>();
		
		Connection conn = null;
		Statement state = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String sql = "SELECT ID FROM " + DB_NAME + ".신청 (WHERE 학기 =" + currentSemester +" and 합격여부 = 'Y' )";
		ResultSet purs = state.executeQuery(sql);
		
		while((line = br.readLine())!=null)	//csvlist에 모든 학번저장
		{
			csvlist.add(line);
		}
		br.close();
		
		Collections.sort(csvlist); // csvlist 오름차순 정렬 끗
		
		while(purs.next())
		{
			if(csvlist.contains(purs.getString("학번")))
			{
				String sql1 = "UPDATE "+ DB_NAME + "SET 납부여부='Y' WHERE 학번 ="+purs.getString("학번");
				ResultSet purs2 = state.executeQuery(sql1);
			}
		}
	}
}
