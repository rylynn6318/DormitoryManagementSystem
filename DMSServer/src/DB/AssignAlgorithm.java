package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import batch.AssignRoomInfo;
import batch.MakeAllRoomInfo;

public class AssignAlgorithm 
{
	private static int currentSemester;
	
	public static void lastPassUpdate() throws SQLException
	{
		String sql = "UPDATE Prototype.신청 A  INNER JOIN Prototype.서류 B ON A.학번 = B.학번 SET A.최종결과 = 'Y' WHERE B.유효여부 = 'Y' AND A.납부여부 = 'Y' AND A.합격여부 = 'Y'";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		state.executeUpdate(sql);
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static void setCurrentSemester() throws ClassNotFoundException, SQLException
	{
		String sql = "SELECT `생활관정보_학기` FROM " + DBHandler.DB_NAME + ".신청 ORDER BY `생활관정보_학기` DESC LIMIT 1"; //신청테이블에서 하나만 가져와서 그 학기를 봄
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery(sql);
		rs.next();
		currentSemester = rs.getInt("생활관정보_학기");
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static AssignRoomInfo[] insertNewBoarderP(AssignRoomInfo[] AR, ResultSet rs, String dormitoryName, ArrayList<String> arr) throws SQLException
	{
		if(rs.next())
		{
			rs.previous();
			while(rs.next())
			{
				if(arr.contains(rs.getString("학번")) != true && rs.getString("생활관정보_생활관명").equals(dormitoryName))			
				{
					for (int i = 0; i < AR.length; i++)
					{
						if(AR[i].getStudentId() == null && AR[i].getSeat().equals("B") && AR[i].getRoomNumber().compareTo("500호") < 0)
						{
								AR[i].setStudentId(rs.getString("학번"));
								AR[i].setIsNew(true);
								if(rs.getInt("지망") == 0)
								{
									AR[i].setCheckout(0);
								}
								break;
						}
					}
				}
				else if(arr.contains(rs.getString("학번")) != true && rs.getString("생활관정보_생활관명").equals(dormitoryName + " 탑층"))		
				{
					for (int i = 0; i < AR.length; i++)
					{
						if(AR[i].getStudentId() == null && AR[i].getSeat().equals("B") && AR[i].getRoomNumber().compareTo("500호") >= 0)
						{
								AR[i].setStudentId(rs.getString("학번"));
								AR[i].setIsNew(true);
								if(rs.getInt("지망") == 0)
								{
									AR[i].setCheckout(0);
								}
								break;
						}
					}
				}
			}
		}
		return AR;
	}
	
	public static AssignRoomInfo[] insertNewBoarder(AssignRoomInfo[] AR, ResultSet rs, String dormitoryName, ArrayList<String> arr) throws SQLException
	{
		if(rs.next())
		{
			rs.previous();
			while(rs.next())
			{
				if(arr.contains(rs.getString("학번")) != true && rs.getString("생활관정보_생활관명").equals(dormitoryName))			
				{
					for (int i = 0; i < AR.length; i++)
					{
						if(AR[i].getStudentId() == null)
						{
							AR[i].setStudentId(rs.getString("학번"));
							AR[i].setIsNew(true);
							if(rs.getInt("지망") == 0)
							{
								AR[i].setCheckout(0);
							}
							break;
						}
					}
					break;
				}
			}
		}
		return AR;
	}
	
				
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		String sql = "SELECT `호`, `생활관명`, `학기` FROM " + DBHandler.DB_NAME + ".호실정보";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement state = connection.prepareStatement(sql);
		ResultSet rs = state.executeQuery(sql);
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// DB에 있는 생활관 호실 정보를 가져와서 서버 메모리 에 올림
		while(rs.next())
		{
			switch (rs.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 0; i < O1.length; i++)
				{
					if(O1[i].getRoomNumber() == null)
					{
						O1[i].setRoomNumber(rs.getString("호"));
						O1[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 0; i < O2.length; i++)
				{
					if(O2[i].getRoomNumber() == null)
					{
						O2[i].setRoomNumber(rs.getString("호"));
						O2[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 0; i < O3.length; i++)
				{
					if(O3[i].getRoomNumber() == null)
					{
						O3[i].setRoomNumber(rs.getString("호"));
						O3[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 0; i < P1.length; i++)
				{
					if(P1[i].getRoomNumber() == null)
					{
						P1[i].setRoomNumber(rs.getString("호"));
						P1[i].setSemesterCode(rs.getInt("학기"));
					}
				}
			}
			case "푸름2" :
			{
				for(int i = 0; i < P2.length; i++)
				{
					if(P2[i].getRoomNumber() == null)
					{
						P2[i].setRoomNumber(rs.getString("호"));
						P2[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 0; i < P3.length; i++)
				{
					if(P3[i].getRoomNumber() == null)
					{
						P3[i].setRoomNumber(rs.getString("호"));
						P3[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 0; i < P4.length; i++)
				{
					if(P4[i].getRoomNumber() == null)
					{
						P4[i].setRoomNumber(rs.getString("호"));
						P4[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 0; i < SN.length; i++)
				{
					if(SN[i].getRoomNumber() == null)
					{
						SN[i].setRoomNumber(rs.getString("호"));
						SN[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 0; i < SY.length; i++)
				{
					if(SY[i].getRoomNumber() == null)
					{
						SY[i].setRoomNumber(rs.getString("호"));
						SY[i].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//배정내역을 불러와서 서버 메모리에 있는 해당 생활관 호실에 배정내역이 있으면 생활관 호실에 학번을 넣어줌
		String sql1 = "SELECT `호실정보_호`, `자리`, `학생_학번`, `호실정보_생활관명` FROM "+DBHandler.DB_NAME+".배정내역 WHERE `퇴사예정일` > (SELECT  시작일  FROM Prototype.스케쥴 WHERE `비고` = '" +currentSemester+"')";// 여러 배정내역 (몇년 전꺼까지도) 중에서 아직 쓰고있는 방 예를들어 지금 2학기인데 1학기 1년 입사자
							//호 옆에 생활관명 넣어야함
		PreparedStatement state1 = connection.prepareStatement(sql1);
		ResultSet rs1 = state1.executeQuery(sql1);
		ArrayList<String> arr = new ArrayList<String>();
		if(rs1.next())
		{
			rs1.previous();
		while(rs1.next())
		{
			arr.add(rs1.getString("학생_학번"));
			switch (rs1.getString("호실정보_생활관명"))
			{
			case "오름1" :
			{
				for(int i = 0; i < O1.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(O1[i].getRoomNumber()) && rs1.getString("자리").equals(O1[i].getSeat()))
					{
						O1[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 0; i < O2.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(O2[i].getRoomNumber()) && rs1.getString("자리").equals(O2[i].getSeat()))
					{
						O2[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 0; i < O3.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(O3[i].getRoomNumber()) && rs1.getString("자리").equals(O3[i].getSeat()))
					{
						O3[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 0; i < P1.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P1[i].getRoomNumber()) && rs1.getString("자리").equals(P1[i].getSeat()))
					{
						P1[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "푸름2" :
			{
				for(int i = 0; i < P2.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P2[i].getRoomNumber()) && rs1.getString("자리").equals(P2[i].getSeat()))
					{
						P2[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 0; i < P3.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P3[i].getRoomNumber()) && rs1.getString("자리").equals(P3[i].getSeat()))
					{
						P3[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 0; i < P4.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P4[i].getRoomNumber()) && rs1.getString("자리").equals(P4[i].getSeat()))
					{
						P4[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 0; i < SN.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(SN[i].getRoomNumber()) && rs1.getString("자리").equals(SN[i].getSeat()))
					{
						SN[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 0; i < SY.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(SY[i].getRoomNumber()) && rs1.getString("자리").equals(SY[i].getSeat()))
					{
						SY[i].setStudentId(rs1.getString("학생_학번"));
						break;
					}
				}
				break;
			}
			}
		}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//신청자가 신청한 생활관을 가져와서 메모리에 있는 현재 배정된 내역과 대조하면서 방에 넣어줌
		sql = "SELECT `학번`, `생활관정보_생활관명`, `지망` FROM " + DBHandler.DB_NAME + ".신청 WHERE `최종결과` = 'Y' order by `생활관정보_생활관명`, `코골이여부`";  // 최종결과가 Y인 신청에 대해 정보를 가져옴
		PreparedStatement state2 = connection.prepareStatement(sql);
		ResultSet rs2 = state2.executeQuery(sql);
		O1 = insertNewBoarder(O1, rs2, "오름1", arr);
		O2 = insertNewBoarder(O2, rs2, "오름2", arr);
		O3 = insertNewBoarder(O3, rs2, "오름3", arr);
		P1 = insertNewBoarderP(P1, rs2, "푸름1", arr);
		P2 = insertNewBoarderP(P2, rs2, "푸름2", arr);
		P3 = insertNewBoarderP(P3, rs2, "푸름3", arr);
		P4 = insertNewBoarderP(P4, rs2, "푸름4", arr);
		SN = insertNewBoarderP(SN, rs2, "신평남", arr);
		SY = insertNewBoarderP(SN, rs2, "신평여", arr);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//이제 업데이트
		PreparedStatement state3 = connection.prepareStatement(sql);
		int insertCurrentSemester;
		for(int i = 0; i < O1.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(O1[i].getIsNew() != null && O1[i].getIsNew())
			{
				if(O1[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + O1[i].getStudentId() + "', '" + O1[i].getRoomNumber() + "', '"+ O1[i].getSemesterCode() + "', '신평여', '" + O1[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < O2.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(O2[i].getIsNew() != null && O2[i].getIsNew())
			{
				if(O2[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + O2[i].getStudentId() + "', '" + O2[i].getRoomNumber() + "', '"+ O2[i].getSemesterCode() + "', '신평여', '" + O2[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < O3.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(O3[i].getIsNew() != null && O3[i].getIsNew())
			{
				if(O3[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + O3[i].getStudentId() + "', '" + O3[i].getRoomNumber() + "', '"+ O3[i].getSemesterCode() + "', '신평여', '" + O3[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < P1.length; i++)
		{			
			insertCurrentSemester = currentSemester;
			if(P1[i].getIsNew() != null && P1[i].getIsNew())
			{
				if(P1[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P1[i].getStudentId() + "', '" + P1[i].getRoomNumber() + "', '"+ P1[i].getSemesterCode() + "', '신평여', '" + P1[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < P2.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(P2[i].getIsNew() != null && P2[i].getIsNew())
			{
				if(P2[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P2[i].getStudentId() + "', '" + P2[i].getRoomNumber() + "', '"+ P2[i].getSemesterCode() + "', '신평여', '" + P2[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < P3.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(P3[i].getIsNew() != null && P3[i].getIsNew())
			{
				if(P3[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P3[i].getStudentId() + "', '" + P3[i].getRoomNumber() + "', '"+ P3[i].getSemesterCode() + "', '신평여', '" + P3[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < P4.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(P4[i].getIsNew() != null && P4[i].getIsNew())
			{
				if(P4[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P4[i].getStudentId() + "', '" + P4[i].getRoomNumber() + "', '"+ P4[i].getSemesterCode() + "', '신평여', '" + P4[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < SN.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(SN[i].getIsNew() != null && SN[i].getIsNew())
			{
				if(SN[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + SN[i].getStudentId() + "', '" + SN[i].getRoomNumber() + "', '"+ SN[i].getSemesterCode() + "', '신평여', '" + SN[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		for(int i = 0; i < SY.length; i++)
		{
			insertCurrentSemester = currentSemester;
			if(SY[i].getIsNew() != null && SY[i].getIsNew())
			{
				if(SY[i].getCheckOut() == 0)
				{
					insertCurrentSemester = (currentSemester / 10) * 10 + 4;
				}
				sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + SY[i].getStudentId() + "', '" + SY[i].getRoomNumber() + "', '"+ SY[i].getSemesterCode() + "', '신평여', '" + SY[i].getSeat() + "', (SELECT DATE_SUB(종료일, INTERVAL 5 day)FROM "+DBHandler.DB_NAME+".스케쥴 WHERE `비고` = '"+ insertCurrentSemester + "'))";
				state3.executeUpdate(sql);
			}
		}
		state.close();
		state1.close();
		state2.close();
		state3.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	public static void batchStart() throws ClassNotFoundException, SQLException
	{
		lastPassUpdate();
		setCurrentSemester();
		residenceUpdate();
	}
}
