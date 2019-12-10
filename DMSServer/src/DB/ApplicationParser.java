package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.omg.CORBA.StringHolder;

import enums.Bool;
import enums.Grade;
import models.Application;
import models.Score;

public class ApplicationParser {
	public static boolean isExist(String studentID) throws SQLException
	{
		String sql = "SELECT 학번 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = "+ studentID;
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		boolean isNext = false;
		
		if(rs.next())
		{
			isNext = true;
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return isNext;
	}
	
	public static boolean deleteApplication(Application app) throws SQLException
	{
		String sql = "DELETE FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번='" + app.getStudentId() + "' AND 생활관정보_생활관명='" + app.getDormitoryName() + "' AND 생활관정보_학기='" + app.getSemesterCode() + "' AND 지망='" + app.getChoice()+"'";
		
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		System.out.println(sql);
				
		try
		{
			connection = DBHandler.INSTANCE.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();
			if(result==0)
			{
				System.out.println("삭제 실패");
				preparedStatement.close();
				DBHandler.INSTANCE.returnConnection(connection);
				return false;
			}
			else
			{
				preparedStatement.close();
				DBHandler.INSTANCE.returnConnection(connection);
				return true;
			}
		}
		catch(SQLException e)
		{
			System.out.println("신청 내역 삭제 쿼리문 실패");
			return false;
		}
		
		
	}
	public static void deleteApplication(String id) throws SQLException, ClassNotFoundException
	{
		int semester = CurrentSemesterParser.getCurrentSemester();
		String sql = "DELETE FROM " + DBHandler.DB_NAME + ".신청 WHERE ( 학번='" + id + "' AND 생활관정보_학기='"+String.valueOf(semester)+"')";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate(sql);
		
		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static ArrayList<Application> getAllApplications() throws SQLException
	{
		String getUnsortedAppsQuery = "SELECT * FROM " + DBHandler.DB_NAME + ".신청";
		System.out.println(getUnsortedAppsQuery);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(getUnsortedAppsQuery);
		ResultSet apps = preparedStatement.executeQuery();
		
		ArrayList<Application> ApplicationList = new ArrayList<Application>();
		while(apps.next())
		{
			Application temp = new Application(apps.getString("학번"), apps.getString("생활관정보_생활관명"), apps.getString("생활관정보_성별"), apps.getInt("생활관정보_학기"), apps.getInt("지망"), apps.getInt("몇일식"), apps.getString("납부여부"), apps.getString("합격여부"), apps.getString("최종결과"), apps.getString("코골이여부"));
			ApplicationList.add(temp);
		}
		
		apps.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return ApplicationList;
	}

	public static int getSemester() throws SQLException 
	{
		String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 학기 = (SELECT max(학기) FROM " + DBHandler.DB_NAME + ".신청)";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		int result = -1;
		if(rs.next())
		{
			result = rs.getInt("학기");	
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return result;
	}
	
	public static int getNumOfLeftSeat(String dormName, int semester) throws SQLException
	{
		String getNumOfPassedAppsQuery = "SELECT COUNT(*) FROM (SELECT * FROM" + DBHandler.DB_NAME + ".배정내역 WHERE 생활관명=" + dormName + " AND 학기=" + semester + " AND 합격여부=Y)";
		String getCapacityQuery = "SELECT 수용인원 FROM 생활관정보 WHERE 생활관명=" + dormName + "AND 학기=" + semester;

		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement passedState = connection.prepareStatement(getNumOfPassedAppsQuery);
		PreparedStatement capacityState = connection.prepareStatement(getCapacityQuery);

		ResultSet passed = passedState.executeQuery();
		ResultSet capacity = capacityState.executeQuery();
		
		capacity.next();
		passed.next();

		int result = capacity.getInt("수용인원") - passed.getInt("COUNT(*)");
		
		if((semester%10) != 01)		//0지망 처리 구문
		{
			int firstSemester = (semester / 10) * 10 + 1;
			String getNumOfZeroChoiceQuery = "SELECT COUNT(*) FROM (SELECT * FROM " + DBHandler.DB_NAME + ".배정내역 WHERE 생활관명=" + dormName + " AND 학기=" + firstSemester + " AND 합격여부=Y AND 지망=0)";
			PreparedStatement passedZeroState = connection.prepareStatement(getNumOfZeroChoiceQuery);
			ResultSet passedZero = passedZeroState.executeQuery();
			passedZero.next();
			result -= passedZero.getInt("COUNT(*)");
		}

		passedState.close();
		capacityState.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return result;
	}

	public static TreeSet<Application> getSortedApps(String dormName, int choice, int semester) throws SQLException, ClassNotFoundException 
	{
		TreeSet<Application> sortedApps = new TreeSet<Application>();
		
		String getUnsortedAppsQuery = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 생활관명=" + dormName + " AND 지망=" + choice + "학기=201901 AND 합격여부=N";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(getUnsortedAppsQuery);
		ResultSet apps = preparedStatement.executeQuery();
		
		while(apps.next())
		{
			Application temp = new Application(apps.getString("학번"), apps.getString("생활관정보_생활관명"), apps.getString("생활관정보_성별"), apps.getInt("생활관정보_학기"), apps.getInt("지망"), getFinalScore(apps.getString("학번"), apps.getInt("학기")));
			
//			if(choice != 01)		//최소 지망일 때는 스킵하게 하고싶은데 어떤건 0지망이 제일 낮고 어떤건 1지망이 제일 낮아서 생각중임
//			{
				String havePassedApp = "SELECT COUNT(*) FROM (SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 합격여부=Y AND 학기=" + temp.getSemesterCode();
				PreparedStatement havePassedAppState = connection.prepareStatement(havePassedApp);
				ResultSet numOfPassed = havePassedAppState.executeQuery();
				numOfPassed.next();
				if(numOfPassed.getInt("COUNT(*)") == 0)
					sortedApps.add(temp);
//			}
			
			
		}

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return sortedApps;
	}
	
//	여기서부터--------------------------------------------------------
	public static double getFinalScore(String studentId, int semester) throws ClassNotFoundException, SQLException
	{	
		TreeSet<Score> score = ApplicationParser.getScores(studentId, pastTwo(semester), pastOne(semester));

		double sumOfTakenGrade = 0;
		double sumOfTakenCredit = 0;
		
		Iterator<Score> scores = score.iterator();
		while(scores.hasNext())
		{
			Score temp = scores.next();
			sumOfTakenCredit += temp.credit;
			
			switch(temp.grade) 
			{
			case APLUS:
				sumOfTakenGrade += 4.5 * temp.credit;
				break;
			case A:
				sumOfTakenGrade += 4 * temp.credit;
				break;
			case BPLUS:
				sumOfTakenGrade += 3.5 * temp.credit;
				break;
			case B:
				sumOfTakenGrade += 3 * temp.credit;
				break;
			case CPLUS:
				sumOfTakenGrade += 2.5 * temp.credit;
				break;
			case C:
				sumOfTakenGrade += 2 * temp.credit;
				break;
			case DPLUS:
				sumOfTakenGrade += 1.5 * temp.credit;
				break;
			case D:
				sumOfTakenGrade += 1 * temp.credit;
				break;
			case F:
				break;
			}
		}
		
		return (sumOfTakenGrade/sumOfTakenCredit + getDistanceScore(ApplicationParser.getZipCode(studentId)));
	}
	
	public static double getDistanceScore(String s)
	{
		int a = Integer.parseInt(s);		
		if(a/100 == 402) return 0.4;	//울릉도
		
		a = a/1000;
		if(a==63) return 0.4;	//제주도
		else if(35 <a && a<44) return 0.1;	//경북, 대구
		else if(43 <a && a<54) return 0.2;	//울산, 부산, 경남
		else if(33 <a && a<36) return 0.2;	//대전
		else
			return 0.3;
	}

	
	public static int pastOne(int semester)
	{
		String pureSemester = String.valueOf(semester).substring(4);	//학기부분만 잘라냄 ex)201901에서 01
		
		switch(pureSemester)
		{
		case "01":				//1학기
			semester -= 98;
			break;
		case "02":				//여름 계절
			semester -= 99;
			break;
		case "03":				//2학기
			semester -= 2;
			break;
		case "04":				//겨울 계절
			semester -= 3;
			break;

		}
		
		return semester;
	}
	
	public static int pastTwo(int semester)
	{
		String pureSemester = String.valueOf(semester).substring(4);	//학기부분만 잘라냄 ex)201901에서 01
		
		switch(pureSemester)
		{
		case "01":
		case "03":
			semester -= 100;
			break;
		case "02":
		case "04":
			semester -= 101;
			break;
		}
		
		return semester;
	}
//	여기까지--------------------------------------------------------는 getSortedApps를 위한 로직임
	
	public static TreeSet<Score> getScores(String studentId, int twoSemesterBefore, int lastSemester) throws SQLException
	{
		String getScoresQuery = "SELECT 학점,등급 FROM " + DBHandler.DB_NAME + ".점수 WHERE 학번=" + studentId + "AND 학기 BETWEEN '" + twoSemesterBefore + "' AND '" + lastSemester + "'";	//직전 2학기 점수 테이블 가져오는 쿼리
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(getScoresQuery);
		ResultSet scores = preparedStatement.executeQuery();

		TreeSet<Score> score = new TreeSet<Score>();
		while(scores.next())
		{	
			score.add(new Score(null, null, 0, scores.getInt("학점"), Grade.get(scores.getString("등급"))));
		}

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return score;
	}
	
	public static String getZipCode(String studentId) throws SQLException
	{
		String zipCodeQuery = "SELECT 보호자우편번호 FROM " + DBHandler.DB_NAME + ".학생 WHERE 학번=" + studentId;
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(zipCodeQuery);
		ResultSet zipCode = preparedStatement.executeQuery();

		zipCode.next();

		String zipcode = zipCode.getString("보호자우편번호");

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return zipcode;
	}

	public static void updatePasser(Application temp) throws SQLException 
	{
		String setPassed = "UPDATE " + DBHandler.DB_NAME + ".신청 SET 합격여부=Y WHERE 학번=" + temp.getStudentId() + "지망=" + temp.getChoice() + "학기=" + temp.getSemesterCode();
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(setPassed);
		preparedStatement.executeUpdate();
		
		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static void updatePayCheck(Application temp) throws SQLException 
	{		
		String a;
		if(temp.isPaid().bool)
			a="Y";
		else
			a="N";
		
		String sql = "UPDATE  "+DBHandler.INSTANCE.DB_NAME+".신청 SET 납부여부 = '"+a+ "' WHERE 학번='"+temp.getStudentId()+"' AND 생활관정보_생활관명 = '"+temp.getDormitoryName()+"' AND 생활관정보_학기 = '"+temp.getSemesterCode()+"' AND 생활관정보_성별 ='"+temp.getGender()+"'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();		
		PreparedStatement state= connection.prepareStatement(sql);
		state.executeUpdate(sql);
		
		state.close();		
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	 
	public static void insertApplication(int choice, int mealType, Bool isSnore, String dormitoryName, char gender , int semesterCode, String id) throws SQLException
	{
		//19.12.09 지금 디비가 안바껴서 안돌아감 -동현-
		System.out.println(gender);
		String snore = isSnore==Bool.TRUE ?"Y" : "N";
		String sql = "INSERT INTO " +DBHandler.INSTANCE.DB_NAME+".신청 VALUES('"+id+"','"+dormitoryName+"','"+String.valueOf(gender)+"','"+ String.valueOf(semesterCode)+"','"+String.valueOf(choice)+"','"+String.valueOf(mealType)+"','N','N','N','"+snore+"')";
//원래  	String sql = "INSERT INTO " +DBHandler.INSTANCE.DB_NAME+".신청 VALUES( "+id+","+dormitoryName+","+String.valueOf(gender)+","+String.valueOf(semesterCode)+","+ String.valueOf(choice)+","+String.valueOf(mealType)+","+"N , N, N ,"+ String.valueOf(isSnore)+")";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		System.out.println(sql);
		state.executeUpdate(sql);
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static Boolean isExistPassState(String id) throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT 학번 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = '" + id + "' and 합격여부 = 'Y' AND 생활관정보_학기 = " + CurrentSemesterParser.getCurrentSemester();
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		if(rs.next())
		{
			state.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return true;
		}
		state.close();		
		DBHandler.INSTANCE.returnConnection(connection);
		return false;
	}
	
	//이거만든놈 누구야 -명근 (2019-12-09 오후 6:43 고침)
	public static ArrayList<Application> getApplicationResult(String id) throws ClassNotFoundException, SQLException
	{
		ArrayList<Application> application = new ArrayList<>();
		String sql = "SELECT 지망, 생활관정보_생활관명, 몇일식 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = '"+ id + "' AND 생활관정보_학기 = '" +CurrentSemesterParser.getCurrentSemester() + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		while(rs.next())
		{
			application.add(new Application(rs.getInt("지망"), rs.getString("생활관정보_생활관명"), rs.getInt("몇일식")));
		}
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return application;
	}
	
	//이것도 누구야 -명근 (2019-12-09 오후 6:43 WHERE 이후 AND연산자 빠짐. FORM이후 띄워쓰기안됨.)
	//그리고 합격여부 Y인것만 가져와야지, 왜 그냥 가져오냐. 대가리박아라 진짜
	public static ArrayList<Application> getPassedApplication(String id) throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT 지망, 생활관정보_생활관명, 몇일식, 합격여부, 납부여부 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = '"+ id + "' AND 생활관정보_학기 = '" +CurrentSemesterParser.getCurrentSemester() + "' AND 합격여부 = 'Y'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		ArrayList<Application> applications = new ArrayList<>();
		while(rs.next())
		{
			Application app = new Application(rs.getInt("지망"), rs.getString("생활관정보_생활관명"), rs.getInt("몇일식"), Bool.get(rs.getString("합격여부")), Bool.get(rs.getString("납부여부")));
			applications.add(app);
			System.out.println(app.getChoice() + ", " + app.getDormitoryName() + ", " + app.getMealType() + ", " + app.isPassed() + ", " + app.isPaid());
		}
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return applications;
	}
	
	public static ArrayList<Application> getPassedApplication() throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 생활관정보_학기 = '" +CurrentSemesterParser.getCurrentSemester() + "' AND 합격여부 = 'Y'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		ArrayList<Application> applications = new ArrayList<Application>();
		while(rs.next())
		{
			Application app = new Application(rs.getString("학번"), rs.getString("생활관정보_생활관명"), rs.getString("생활관정보_성별"), rs.getInt("생활관정보_학기"), rs.getInt("지망"), rs.getInt("몇일식"), rs.getString("납부여부"), rs.getString("합격여부"), rs.getString("최종결과"), rs.getString("코골이여부"));
			applications.add(app);
		}
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return applications;
	}
	
	public static Application getLastPassedApplication(String id) throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT 최종결과, 납부여부, 몇일식, 생활관정보_생활관명 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = '"+ id + "' AND 생활관정보_학기 = '" + CurrentSemesterParser.getCurrentSemester() + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		Application application = null;
		if(rs.next())
			application = new Application(Bool.get(rs.getString("최종결과")), Bool.get(rs.getString("납부여부")), rs.getInt("몇일식"), rs.getString("생활관정보_생활관명"));
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return application;
	}
	
	public static Boolean isExistLastPass(String id) throws SQLException, ClassNotFoundException
	{
		String sql = "SELECT 최종합격여부" + DBHandler.DB_NAME + ".신청 WHERE 학번 = '"+ id + "' AND 생활관정보_학기 = '" + CurrentSemesterParser.getCurrentSemester() + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		
		boolean isExist = false;
		
		if(rs.next())
		{
			isExist = true;
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return isExist;
	}

	public static boolean isExist(String studentId, String dormitoryName, int semesterCode) throws SQLException 
	{
		String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = '"+ studentId + "' AND 생활관정보_생활관명 = '" + dormitoryName + "' AND 생활관정보_학기 = '" + semesterCode + "'";
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();

		boolean isExist = false;
		
		if(rs.next())
		{
			isExist = true;
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return isExist;
	}

}
