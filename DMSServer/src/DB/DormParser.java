package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		Connection connection = DBHandler.INSTANCE.getConnetion();
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
		Connection connection = DBHandler.INSTANCE.getConnetion();
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

		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".생활관정보  WHERE 성별 = " + gender.gender + " AND 학기 = " + String.valueOf(semester);
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet resultSet = state.executeQuery();

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
	
	public static int getCheckBillCost(String id) throws ClassNotFoundException, SQLException
	{
		int cost;
		String sql = "SELECT 몇일식, 생활관명 FROM " + DBHandler.DB_NAME + ".신청 WHERE 학번=" + id + "and 합격여부 = 'Y' and 생활관정보_학기 = " + CurrentSemesterParser.getCurrentSemester();
		Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		rs.next();
		String sql1 = "SELECT "+rs.getInt("몇일식")+"일식, 기숙사비 FROM " + DBHandler.DB_NAME + ".생활관정보 WHERE 생활관명 = "+ rs.getString("생활관명") + "and 학기 = " + CurrentSemesterParser.getCurrentSemester();
		PreparedStatement state1 = connection.prepareStatement(sql1);
		ResultSet rs1 = state1.executeQuery();
		rs1.next();
		cost = rs1.getInt(rs.getInt("몇일식")+"일식") + rs1.getInt("기숙사비");
		
		return cost;
	}
}