package assign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pass 
{
	static final String DRIVER_NAME = "mysql";
	static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	static final String PORT = "3306";
	static final String DB_NAME = "Prototype";													//DB이름
	static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 									//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	
	public static void passUpdate() throws SQLException, ClassNotFoundException
	{	
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		Statement state2 = conn.createStatement();
		
		String sql = "SELECT ID, 납부여부, 합격여부, 최종결과 FROM " + DB_NAME + ".신청";
		ResultSet purs = state.executeQuery(sql);		
		while(purs.next())
		{
			if(purs.getString("납부여부").equals("Y") && purs.getString("합격여부").equals("Y"))
			{
				String query = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + purs.getInt("ID")+")";   // 납부여부 Y 합격여부 Y면 해당 ID의 최종결과 Y
				state2.executeUpdate(query);
			} 
		}

	}
	
	public static void residenceUpdate() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] O1 = MakeAllRoomInfo.getO1();
		AssignRoomInfo[] O2 = MakeAllRoomInfo.getO2();
		AssignRoomInfo[] O3 = MakeAllRoomInfo.getO3();
		AssignRoomInfo[] P1 = MakeAllRoomInfo.getP1();
		AssignRoomInfo[] P2 = MakeAllRoomInfo.getP2();
		AssignRoomInfo[] P3 = MakeAllRoomInfo.getP3();
		AssignRoomInfo[] P4 = MakeAllRoomInfo.getP4();
		AssignRoomInfo[] SN = MakeAllRoomInfo.getSN();
		AssignRoomInfo[] SY = MakeAllRoomInfo.getSY();
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		String sql1 = "SELECT 호, 생활관명 FROM " + DB_NAME + ".호실정보";
		ResultSet rurs1 = state.executeQuery(sql1);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// DB에 있는 생활관 호실 정보를 가져와서 서버 메모리 에 올림
		while(rurs1.next())
		{
			switch (rurs1.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i += 2)
				{
					if(O1[i].getRoomNumber() == "")
					{
						O1[i].setRoomNumber(rurs1.getString("호"));
						O1[i + 1].setRoomNumber(rurs1.getString("호"));
						O1[i].setSemesterCode(201901);
						O1[i].setSemesterCode(201901);
					}
				}
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i += 2)
				{
					if(O2[i].getRoomNumber() == "")
					{
						O2[i].setRoomNumber(rurs1.getString("호"));
						O2[i + 1].setRoomNumber(rurs1.getString("호"));
						O2[i].setSemesterCode(201901);
					}
				}
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i += 2)
				{
					if(O3[i].getRoomNumber() == "")
					{
						O3[i].setRoomNumber(rurs1.getString("호"));
						O3[i + 1].setRoomNumber(rurs1.getString("호"));
						O3[i].setSemesterCode(201901);
					}
				}
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i += 2)
				{
					if(P1[i].getRoomNumber() == "")
					{
						P1[i].setRoomNumber(rurs1.getString("호"));
						P1[i + 1].setRoomNumber(rurs1.getString("호"));
						P1[i].setSemesterCode(201901);
					}
				}
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i += 2)
				{
					if(P2[i].getRoomNumber() == "")
					{
						P2[i].setRoomNumber(rurs1.getString("호"));
						P2[i + 1].setRoomNumber(rurs1.getString("호"));
						P2[i].setSemesterCode(201901);
					}
				}
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i += 4)
				{
					if(P3[i].getRoomNumber() == "")
					{
						P3[i].setRoomNumber(rurs1.getString("호"));
						P3[i + 1].setRoomNumber(rurs1.getString("호"));
						P3[i + 2].setRoomNumber(rurs1.getString("호"));
						P3[i + 3].setRoomNumber(rurs1.getString("호"));
						P3[i].setSemesterCode(201901);
					}
				}
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i += 2)
				{
					if(P4[i].getRoomNumber() == "")
					{
						P4[i].setRoomNumber(rurs1.getString("호"));
						P4[i + 1].setRoomNumber(rurs1.getString("호"));
						P4[i + 2].setRoomNumber(rurs1.getString("호"));
						P4[i + 3].setRoomNumber(rurs1.getString("호"));
						P4[i].setSemesterCode(201901);
					}
				}
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i += 2)
				{
					if(SN[i].getRoomNumber() == "")
					{
						SN[i].setRoomNumber(rurs1.getString("호"));
						SN[i + 1].setRoomNumber(rurs1.getString("호"));
						SN[i].setSemesterCode(201901);
					}
				}
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i += 2)
				{
					if(SY[i].getRoomNumber() == "")
					{
						SY[i].setRoomNumber(rurs1.getString("호"));
						SY[i + 1].setRoomNumber(rurs1.getString("호"));
						SY[i].setSemesterCode(201901);
					}
				}
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//배정내역을 불러와서 서버 메모리에 있는 해당 생활관 호실에 배정내역이 있으면 생활관 호실에 학번을 넣어줌
		String sql2 = "SELECT 호, 생활관명, 자리, ID FROM " + DB_NAME + ".배정내역";
		ResultSet rurs2 = state.executeQuery(sql2);
		
		while(rurs2.next())
		{
			switch (rurs2.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i++)
				{
					if(rurs2.getString("호") == O1[i].getRoomNumber() && rurs2.getString("자리") == O1[i].getSeat())
					{
						O1[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i++)
				{
					if(rurs2.getString("호") == O2[i].getRoomNumber() && rurs2.getString("자리") == O2[i].getSeat())
					{
						O2[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i++)
				{
					if(rurs2.getString("호") == O3[i].getRoomNumber() && rurs2.getString("자리") == O3[i].getSeat())
					{
						O3[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i++)
				{
					if(rurs2.getString("호") == P1[i].getRoomNumber() && rurs2.getString("자리") == P1[i].getSeat())
					{
						P1[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i++)
				{
					if(rurs2.getString("호") == P2[i].getRoomNumber() && rurs2.getString("자리") == P2[i].getSeat())
					{
						P2[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i++)
				{
					if(rurs2.getString("호") == P3[i].getRoomNumber() && rurs2.getString("자리") == P3[i].getSeat())
					{
						P3[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i++)
				{
					if(rurs2.getString("호") == P4[i].getRoomNumber() && rurs2.getString("자리") == P4[i].getSeat())
					{
						P4[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i++)
				{
					if(rurs2.getString("호") == SN[i].getRoomNumber() && rurs2.getString("자리") == SN[i].getSeat())
					{
						SN[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i++)
				{
					if(rurs2.getString("호") == SY[i].getRoomNumber() && rurs2.getString("자리") == SY[i].getSeat())
					{
						SY[i].setStudentId(rurs2.getString("ID"));
					}
				}
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//신청자가 신청한 생활관을 가져와서 메모리에 있는 현재 배정된 내역과 대조하면서 방에 넣어줌
		String sql3 = "SELECT ID, 생활관명 FROM " + DB_NAME + ".신청 WHERE (학기 = 201901 and 최종결과 = 'Y') order by 생활관명";   // 201901은 임시로 넣은것, 제대로 하려면 학기를 유동적으로 바꿀 수 있어야함.
		ResultSet rurs3 = state.executeQuery(sql3);
		
		while(rurs3.next())
		{
			switch (rurs3.getString("생활관명"))
			{
			case "오름1" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					if(O1[i].getStudentId() == "")
					{
						O1[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "오름2" :
			{
				for (int i = 1; i < O2.length; i++)
				{
					if(O2[i].getStudentId() == "")
					{
						O2[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "오름3" :
			{
				for (int i = 1; i < O3.length; i++)
				{
					if(O3[i].getStudentId() == "")
					{
						O3[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "푸름1" :
			{
				for (int i = 1; i < P1.length; i++)
				{
					if(P1[i].getStudentId() == "")
					{
						P1[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "푸름2" :
			{
				for (int i = 1; i < P2.length; i++)
				{
					if(P2[i].getStudentId() == "")
					{
						P2[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "푸름3" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					if(P3[i].getStudentId() == "")
					{
						P3[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "푸름4" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					if(P4[i].getStudentId() == "")
					{
						P4[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "신평남" :
			{
				for (int i = 1; i < SN.length; i++)
				{
					if(SN[i].getStudentId() == "")
					{
						SN[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			case "신평여" :
			{
				for (int i = 1; i < SY.length; i++)
				{
					if(SY[i].getStudentId() == "")
					{
						SY[i].setStudentId(rurs3.getString("ID"));
					}
				}
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//이제 업데이트
		for(int i = 1; i < O1.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + O1[i].getStudentId() + "', '" + O1[i].getSeat()+ "', '"+O1[i].getCheckOut()+ "', '오름1', '" + O1[i].getSemesterCode() + "', '" + O1[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < O2.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + O2[i].getStudentId() + "', '" + O2[i].getSeat()+ "', '"+O2[i].getCheckOut()+ "', '오름1', '" + O2[i].getSemesterCode() + "', '" + O2[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < O3.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + O3[i].getStudentId() + "', '" + O3[i].getSeat()+ "', '"+O3[i].getCheckOut()+ "', '오름1', '" + O3[i].getSemesterCode() + "', '" + O3[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < P1.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P1[i].getStudentId() + "', '" + P1[i].getSeat()+ "', '"+P1[i].getCheckOut()+ "', '오름1', '" + P1[i].getSemesterCode() + "', '" + P1[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < P2.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P2[i].getStudentId() + "', '" + P2[i].getSeat()+ "', '"+P2[i].getCheckOut()+ "', '오름1', '" + P2[i].getSemesterCode() + "', '" + P2[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < P3.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P3[i].getStudentId() + "', '" + P3[i].getSeat()+ "', '"+P3[i].getCheckOut()+ "', '오름1', '" + P3[i].getSemesterCode() + "', '" + P3[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < P4.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P4[i].getStudentId() + "', '" + P4[i].getSeat()+ "', '"+P4[i].getCheckOut()+ "', '오름1', '" + P4[i].getSemesterCode() + "', '" + P4[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < SN.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + SN[i].getStudentId() + "', '" + SN[i].getSeat()+ "', '"+SN[i].getCheckOut()+ "', '오름1', '" + SN[i].getSemesterCode() + "', '" + SN[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		for(int i = 1; i < SY.length; i++)
		{
			String sql5 = "INSERT INTO " + DB_NAME + "배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + SY[i].getStudentId() + "', '" + SY[i].getSeat()+ "', '"+SY[i].getCheckOut()+ "', '오름1', '" + SY[i].getSemesterCode() + "', '" + SY[i].getRoomNumber() +"')";
			state.executeQuery(sql5);
		}
		
		
		
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		passUpdate();
		residenceUpdate();
	}
}
