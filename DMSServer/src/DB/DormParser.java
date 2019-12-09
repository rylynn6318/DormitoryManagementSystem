package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import enums.Bool;
import enums.Gender;
import models.Dormitory;

public class DormParser {	
	//4. 생활관 테이블에서 이번 학기에 해당하고, 성별에 해당하는 기숙사 정보 목록을 가져온다.
	public static ArrayList<String> getDormList(Gender g) throws Exception
	{
		String sql = "SELECT 생활관명 FROM " + DBHandler.INSTANCE.DB_NAME + ".생활관정보  WHERE 성별 = "+ g.gender;

		// ResultSet resultSet = DBHandler.INSTANCE.excuteSelect(sql);
		// 현재 두가지 경우가 있다.
		// 1. DBHandler.INSTANCE.excuteSelect 쓰는 경우
		// 2. 기존의, DB 커넥션 만들어서 쓰는 경우
		// 이 경우엔 1.번 경우로 아래와 같이 수정한다
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();

		ArrayList<String> dlist = new ArrayList<String>();
		while(resultSet.next())
		{
			String s = resultSet.getString("생활관명");
			dlist.add(s);
		}

		// 로직이 끝났으면 반환한다.
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return dlist;
	}
	//5. 가져와야할 정보는 생활관 테이블의 생활관명, 기간구분(없으면말고), 식사구분, 5일식 식비, 7일식 식비, 관리비
	// 일단 싹다 보내주고 클라이언트에서 알아서 거르는걸로
	public static ArrayList<Dormitory> getDormInfo(ArrayList<String> dList) throws Exception
	{
		Connection connection = DBHandler.INSTANCE.getConnection();
		ArrayList<Dormitory> dorm = new ArrayList<Dormitory>();
		ResultSet resultSet = null;
		PreparedStatement state = null;
		for(int i=0;i<dList.size();i++)
		{
			String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".생활관정보  WHERE 생활관명 = "+ dList.get(i);
			state = connection.prepareStatement(sql);
			resultSet = state.executeQuery();
			// TODO : 이 경우는 로직이 잘못된거 아닌가? while 구문이 for 안으로 들어와야 하는거 아님?
		}
		while(resultSet.next())
		{			
			//시원하게 보내라해서 일단 성별로 거른 생활관목록에 대한 모든 정보를 보냄
			Dormitory d = new Dormitory(
					resultSet.getString("생활관명"),
					Gender.get(resultSet.getString("성별")),
					resultSet.getInt("학기"),
					resultSet.getInt("수용인원"),
					Bool.get(resultSet.getString("식사의무")),
					resultSet.getInt("5일식_식비"),
					resultSet.getInt("7일식_식비"),
					resultSet.getInt("기숙사비"));
			dorm.add(d);
		}
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return dorm;
	}
	
	//제대로 동작하는지 모르겠음. 현재 학기에서 성별로 생활관 조회, 모든 결과값을 Dormitory 객체 배열로 반환함. 
	public static ArrayList<Dormitory> getDormitoryList(int semester, Gender gender) throws Exception
	{
		ArrayList<Dormitory> dorm = new ArrayList<Dormitory>();

		System.out.println(gender.gender);
		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".생활관정보  WHERE 성별 = '" + gender.gender + "' AND 학기 = " + String.valueOf(semester);
		System.out.println(sql);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();

		while(resultSet.next())
		{			
			//시원하게 보내라해서 일단 성별로 거른 생활관목록에 대한 모든 정보를 보냄
			String dormName = resultSet.getString("생활관명");
			Gender gender2 = Gender.get(resultSet.getString("성별"));
			int semester2 = resultSet.getInt("학기");
			int capacity = resultSet.getInt("수용인원");
			Bool isMealDuty = Bool.get(resultSet.getString("식사의무"));
			int meal5 = resultSet.getInt("5일식_식비");
			int meal7 = resultSet.getInt("7일식_식비");
			int boardingFee = resultSet.getInt("기숙사비");
			
			System.out.println(dormName);
			
			Dormitory d = new Dormitory(dormName, gender2, semester2, capacity, isMealDuty, meal5, meal7, boardingFee);
			dorm.add(d);
		}

		state.close();
		DBHandler.INSTANCE.returnConnection(connection);

		return dorm;
	}
	
	//조보아씨, 한번 와보세요. 아래가 수정전인데, 
	//SELECT 몇일식, 생활관명 FROM Prototype.신청 WHERE 학번=20161185and 합격여부 = 'Y' and 생활관정보_학기 = 201903
	//sql 띄워쓰기 안해요? sql1도 그렇고 sql2도 안돼있네... 코드 다고침(명근, 2019-12-09 19:38 수정함)
	public static int getCheckBillCost(String id) throws ClassNotFoundException, SQLException
	{
		int curSemester = CurrentSemesterParser.getCurrentSemester();
		String sql1 = "SELECT 몇일식, 생활관정보_생활관명 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번 = '" + id + 
				"' AND 합격여부 = 'Y' and 생활관정보_학기 = '" + curSemester + "'";
		System.out.println(sql1);
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql1);
		ResultSet rs1 = state.executeQuery();
		rs1.next();
		
		int mealType = rs1.getInt("몇일식");
		String mealTypeColumn = String.valueOf(mealType)+"일식_식비"; 
		String dormName = rs1.getString("생활관정보_생활관명");
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		String sql2 = "SELECT `"+ mealTypeColumn +"`, `기숙사비` FROM " + DBHandler.DB_NAME + ".생활관정보 WHERE 생활관명 = '" +
				dormName + "' AND 학기 = '" + curSemester + "'";
		System.out.println(sql2);
		PreparedStatement state1 = connection.prepareStatement(sql2);
		ResultSet rs2 = state1.executeQuery();
		rs2.next();
		
		int mealCost = rs2.getInt(mealTypeColumn);
		System.out.println("식사비 : " + mealCost);
		int boardingFee = rs2.getInt("기숙사비");
		System.out.println("기숙사비 : " + boardingFee);
		
		state1.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return mealCost + boardingFee;
	}
	
	public static int getMaxCapacity(String dormitoryName) throws SQLException, ClassNotFoundException
	{
		int capacity = 10;
		String sql = "SELECT `수용인원` FROM " + DBHandler.DB_NAME + ".생활관정보 WHERE `학기` = '"+ CurrentSemesterParser.getCurrentSemester() +"' and `생활관명` = '" + dormitoryName + "'";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		if(rs.next())
		{
			capacity = rs.getInt("수용인원");
		}
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		return capacity;
	}
}