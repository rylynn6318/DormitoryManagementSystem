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
		Statement state = DBinfo.connection();
		String sql = "SELECT 생활관명 FROM " + DBinfo.DB_NAME + ".생활관정보  WHERE 성별 = "+ g.toString();
		ResultSet purs = state.executeQuery(sql);
		ArrayList<String> dlist = new ArrayList<String>();
		while(purs.next())
		{
			String s = purs.getString("생활관명");			
			dlist.add(s);
		}
		return dlist;
	}
	//5. 가져와야할 정보는 생활관 테이블의 생활관명, 기간구분(없으면말고), 식사구분, 5일식 식비, 7일식 식비, 관리비
	// 일단 싹다 보내주고 클라이언트에서 알아서 거르는걸로
	public static ArrayList<Dormitory> getDormInfo(ArrayList<String> dList) throws Exception
	{
		ArrayList<Dormitory> dorm = new ArrayList<Dormitory>();
		ResultSet purs = null;
		for(int i=0;i<dList.size();i++)
		{
			Statement state = DBinfo.connection();
			String sql = "SELECT * FROM " + DBinfo.DB_NAME + ".생활관정보  WHERE 생활관명 = "+ dList.get(i);
			purs = state.executeQuery(sql);			
		}
		while(purs.next())
		{			
			//시원하게 보내라해서 일단 성별로 거른 생활관목록에 대한 모든 정보를 보냄
			Dormitory d = new Dormitory(purs.getString("생활관명"), Gender.get(purs.getString("성별").charAt(0)), purs.getInt("학기"), purs.getInt("수용인원"), Bool.get(purs.getString("식사의무")),purs.getInt("5일식_식비"),purs.getInt("7일식_식비"),purs.getInt("기숙사비"));
			dorm.add(d);
		}
		return dorm;
	}
	
	//제대로 동작하는지 모르겠음. 현재 학기에서 성별로 생활관 조회, 모든 결과값을 Dormitory 객체 배열로 반환함. 
	public static ArrayList<Dormitory> getDormitoryList(String semester, Gender gender) throws Exception
	{
		ArrayList<Dormitory> dorm = new ArrayList<Dormitory>();
		ResultSet purs = null;
		
		Statement state = DBinfo.connection();
		String sql = "SELECT * FROM " + DBinfo.DB_NAME + ".생활관정보  WHERE 성별 = " + gender.toString() + " AND 학기 = " + semester;
		purs = state.executeQuery(sql);	
		while(purs.next())
		{			
			//시원하게 보내라해서 일단 성별로 거른 생활관목록에 대한 모든 정보를 보냄
			Dormitory d = new Dormitory(purs.getString("생활관명"), Gender.get(purs.getString("성별").charAt(0)), purs.getInt("학기"), purs.getInt("수용인원"), Bool.get(purs.getString("식사의무")),purs.getInt("5일식_식비"),purs.getInt("7일식_식비"),purs.getInt("기숙사비"));
			dorm.add(d);
		}
		return dorm;
	}
}