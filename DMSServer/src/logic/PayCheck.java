package logic;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import DB.DBinfo;
public class PayCheck {
	
	private static int currentSemester;
	
	public static void currentSemester() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		
		String sql = "SELECT 학기 FROM" + DBinfo.DB_NAME + ".신청 limit1"; //신청테이블에서 하나만 가져와서 그 학기를 봄
		ResultSet rcrs = state.executeQuery(sql);
		currentSemester = rcrs.getInt("학기");
	}
	
	public static void main(String[] args) throws IOException, Exception
	{
		//currentSemester=201901;
		
		File csv = new File("C:|Users|DongHyeon|Downloads");
		BufferedReader br = new BufferedReader(new FileReader(csv));
		String line ="";
		ArrayList<String> csvlist = new ArrayList<String>();
		
		Statement state = DBinfo.connection();
		
		String sql = "SELECT 학생_ID FROM " + DBinfo.DB_NAME + ".신청 WHERE (학기 =" + currentSemester +" and 합격여부 = 'Y' )";
		ResultSet purs = state.executeQuery(sql);
		
		while((line = br.readLine())!=null)	//csvlist에 모든 학번저장
		{
			csvlist.add(line);
		}
		br.close();
		
		Collections.sort(csvlist); // csvlist 오름차순 정렬 끗
		
		while(purs.next())
		{
			if(csvlist.contains(purs.getString("학생_ID")))
			{
				Statement state1 = DBinfo.connection();
				String sql1 = "UPDATE "+ DBinfo.DB_NAME + ".신청 SET 납부여부='Y' WHERE ( 학생_ID ="+purs.getString("학생_ID")+")";
				state1.executeUpdate(sql1);
			}
		}
		//System.out.println("성공");
	}
}
