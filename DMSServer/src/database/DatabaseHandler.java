package database;

import java.sql.*;		//사용을 위해 프로젝트명-우클릭-properties-Java Build Path-Libraries-Add External JARS에서 mysql-connector-java-8.0.18.jar추가해줘야됨.

import io.IOHandler;
import io.MsgType;

//데이터베이스에 관한 작업들을 총괄하는 클래스

public class DatabaseHandler
{
	private final int TIMEOUT = 15;																//타임아웃
	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 								//드라이버
	private final String DRIVER_NAME = "mysql";
	private final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	private final String PORT = "3306";
	private final String DB_NAME = "Prototype";													//DB이름
	private final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	private final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 									//사용자의 비밀번호를 상수로 정의
	private final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	
	
	//데이터베이스 접속 테스트 메소드
	public boolean connectionTest()
	{
		boolean isSucceed = false;
		Connection tmpConn = null;
		Statement state = null;
		
		try
		{
			//이해하기 어려운 부분, 드라이버 경로를 전달하는데, 해당 클래스(드라이버)가 없으면 예외발생시킴. 성공하면 DriverManager에 해당 드라이거 등록되어 DB사용 가능해짐.
			Class.forName(JDBC_DRIVER);
			DriverManager.setLoginTimeout(TIMEOUT);								//타임아웃 설정
			tmpConn = DriverManager.getConnection(DB_URL);	//각 정보를 전달하여 접속한 정보를 conn에 저장한다. 연결 실패시 SQLException 발생함.
																				//여기서 timezone 문제가 뜬다면 mysql 서버 타임존 설정이 안된것. https://offbyone.tistory.com/318 참조할것.
			state = tmpConn.createStatement();									//SQL문을 실행하기 위해 conn 연결정보를 state로 생성해야된다. 생성 성공 시 Statement의 executeQuery 메소드로 SQL문 실행 가능.
			
			//연결 종료
			state.close();
			tmpConn.close();
			isSucceed = true;
		}
		catch(Exception e)
		{
			//예외 발생 시 처리부분
			IOHandler.getInstnace().printMsg(MsgType.ERROR, "connectionTest", e.getMessage());
		}
		finally
		{
			try
			{
				if(state != null)
					state.close();
			}
			catch(SQLException ex1)
			{
				//state를 close 하는데 실패.
				IOHandler.getInstnace().printMsg(MsgType.ERROR, "connectionTest", ex1.getMessage());
			}
			
			try
			{
				if(tmpConn != null)
					tmpConn.close();
			}
			catch(SQLException ex2)
			{
				//conn을 close하는데 실패
				IOHandler.getInstnace().printMsg(MsgType.ERROR, "connectionTest", ex2.getMessage());
			}
		}
		return isSucceed;
	}
	
	//데이터베이스 테스트 연결 메소드, 타 개발자의 이해를 위해 임시로 넣어둔거임. 
	//참고 : http://blog.naver.com/PostView.nhn?blogId=lghlove0509&logNo=221031017994&parentCategoryNo=&categoryNo=38&viewDate=&isShowPopularPosts=true&from=search
	public void tempTest()
	{
		Connection conn = null;
		Statement state = null;
		
		try
		{
			//이해하기 어려운 부분, 드라이버 경로를 전달하는데, 해당 클래스(드라이버)가 없으면 예외발생시킴. 성공하면 DriverManager에 해당 드라이거 등록되어 DB사용 가능해짐. 이것을 Reflection이라 한다.
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		//각 정보를 전달하여 접속한 정보를 conn에 저장한다. 연결 실패시 SQLException 발생함.
																					//여기서 timezone 문제가 뜬다면 mysql 서버 타임존 설정이 안된것. https://offbyone.tistory.com/318 참조할것.
			state = conn.createStatement();											//SQL문을 실행하기 위해 conn 연결정보를 state로 생성해야된다. 생성 성공 시 Statement의 executeQuery 메소드로 SQL문 실행 가능.
			//SQL 구문 작성. 현재 구문은 mydb에서 학생 테이블 모두 선택하는 것.
			String sql = "SELECT * FROM " + DB_NAME + ".학생";
			ResultSet rs = state.executeQuery(sql);									//결과값을 담을 ResultSet 객체 생성.
			
			//반환값이 없을때까지 한 줄씩 읽는다.
			while(rs.next())
			{
				String studentId  = rs.getString("ID");
				String name = rs.getString("성명"); 
				String gender = rs.getString("성별");
				String departmentName = rs.getString("학과명");
				String year = rs.getString("학년");
				String rrn = rs.getString("주민등록번호");
				String contact = rs.getString("학생전화번호");
				String parentZipCode = rs.getString("보호자우편번호");
				String parentAddress = rs.getString("보호자주소");
				String medicalCertificatePath = rs.getString("결핵진단서_경로");
				String medicalCertificateCheck = rs.getString("결핵진단서_확인여부");
				
				System.out.println(studentId + ", " + name + ", " + gender + ", " + departmentName + ", " + year + ", " + rrn + ", " + contact + ", " + parentZipCode
						 + ", " + parentAddress + ", " + medicalCertificatePath + ", " + medicalCertificateCheck);
			}
			
			//사용이 끝난 객체들은 close 해준다.
			rs.close();
			state.close();
			conn.close();
			
		}
		catch(Exception e)
		{
			//예외 발생 시 처리부분
			System.out.println("[DatabaseHandler] connectionTest : " + e.getMessage());
		}
		finally
		{
			try
			{
				if(state != null)
					state.close();
			}
			catch(SQLException ex1)
			{
				//state를 close 하는데 실패.
				System.out.println("예외 발생) DatabaseHandler : " + ex1.getMessage());
			}
			
			try
			{
				if(conn != null)
					conn.close();
			}
			catch(SQLException ex2)
			{
				//conn을 close하는데 실패
				System.out.println("예외 발생) DatabaseHandler : " + ex2.getMessage());
			}
		}
		
		System.out.println("테스트커넥트 종료됨.");
	}

	public void prepareShutdown()
	{
		
	}
}
