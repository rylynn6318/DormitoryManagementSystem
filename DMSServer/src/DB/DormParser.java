package DB;

import java.sql.ResultSet;
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

		ResultSet resultSet = DBHandler.INSTANCE.excuteSelect(sql);

		ArrayList<String> dlist = new ArrayList<String>();
		while(resultSet.next())
		{
			String s = resultSet.getString("생활관명");
			dlist.add(s);
		}
		return dlist;
	}
	//5. 가져와야할 정보는 생활관 테이블의 생활관명, 기간구분(없으면말고), 식사구분, 5일식 식비, 7일식 식비, 관리비
	// 일단 싹다 보내주고 클라이언트에서 알아서 거르는걸로
	public static ArrayList<Dormitory> getDormInfo(ArrayList<String> dList) throws Exception
	{
		ArrayList<Dormitory> dorm = new ArrayList<Dormitory>();
		ResultSet resultSet = null;
		for(int i=0;i<dList.size();i++)
		{
			String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".생활관정보  WHERE 생활관명 = "+ dList.get(i);
			resultSet = DBHandler.INSTANCE.excuteSelect(sql);
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
		return dorm;
	}
	
	//제대로 동작하는지 모르겠음. 현재 학기에서 성별로 생활관 조회, 모든 결과값을 Dormitory 객체 배열로 반환함. 
	public static ArrayList<Dormitory> getDormitoryList(String semester, Gender gender) throws Exception
	{
		ArrayList<Dormitory> dorm = new ArrayList<Dormitory>();
		ResultSet resultSet = null;

		String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".생활관정보  WHERE 성별 = " + gender.gender + " AND 학기 = " + semester;
		resultSet = DBHandler.INSTANCE.excuteSelect(sql);
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
		return dorm;
	}
}