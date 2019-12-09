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
	private static int availablePeriod;
	private static int checkOutDate1; //이건 1학기나 계절학기 신청자들의 기숙사 종료일 어차피 현재 받고있는 신청자들의 학기에 따라 변수 알아서 바뀌니 걱정 ㄴ
	private static int checkOutDate2; //이건 1년 신청자들의 기숙사 종료일
	
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
	
	public static void setAvailablePeriod()
	{
		switch(currentSemester % 10)
		{
		case 1:
			availablePeriod = (currentSemester - 1) * 100 + 620; // 1학기
			break;
		case 2:
			availablePeriod = (currentSemester - 2) * 100 + 820; // 여름
			break;
		case 3:
			availablePeriod = (currentSemester - 3) * 100 + 1220; // 2학기
			break;
		case 4:
			availablePeriod = (currentSemester - 4) * 100 + 10220; // 겨울
			break;
		}
	}
	
	public static void setCheckOutPeriod()
	{
		switch(currentSemester % 10)
		{
		case 1:
			checkOutDate1 = (currentSemester - 1) * 100 + 620;	// 1학기 
			checkOutDate2 = (currentSemester - 1) * 100 + 1220; // 1년 신청일 경우
			break;
		case 2:
			checkOutDate1 = (currentSemester - 2) * 100 + 820;	// 여름
			break;
		case 3:
			checkOutDate1 = (currentSemester - 3) * 100 + 10120;	// 2학기
			checkOutDate2 = (currentSemester - 3) * 100 + 1220;
			break;
		case 4:
			checkOutDate1 = (currentSemester - 4) * 100 + 10120; //	겨울
			break;
		}
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
				for(int i = 1; i < O1.length; i += 2)
				{
					if(O1[i].getRoomNumber() == null)
					{
						O1[i].setRoomNumber(rs.getString("호"));
						O1[i + 1].setRoomNumber(rs.getString("호"));
						O1[i].setSemesterCode(rs.getInt("학기"));
						O1[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i += 2)
				{
					if(O2[i].getRoomNumber() == null)
					{
						O2[i].setRoomNumber(rs.getString("호"));
						O2[i + 1].setRoomNumber(rs.getString("호"));
						O2[i].setSemesterCode(rs.getInt("학기"));
						O2[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i += 2)
				{
					if(O3[i].getRoomNumber() == null)
					{
						O3[i].setRoomNumber(rs.getString("호"));
						O3[i + 1].setRoomNumber(rs.getString("호"));
						O3[i].setSemesterCode(rs.getInt("학기"));
						O3[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i += 2)
				{
					if(P1[i].getRoomNumber() == null)
					{
						P1[i].setRoomNumber(rs.getString("호"));
						P1[i + 1].setRoomNumber(rs.getString("호"));
						P1[i].setSemesterCode(rs.getInt("학기"));
						P1[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i += 2)
				{
					if(P2[i].getRoomNumber() == null)
					{
						P2[i].setRoomNumber(rs.getString("호"));
						P2[i + 1].setRoomNumber(rs.getString("호"));
						P2[i].setSemesterCode(rs.getInt("학기"));
						P2[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i += 4)
				{
					if(P3[i].getRoomNumber() == null)
					{
						P3[i].setRoomNumber(rs.getString("호"));
						P3[i + 1].setRoomNumber(rs.getString("호"));
						P3[i + 2].setRoomNumber(rs.getString("호"));
						P3[i + 3].setRoomNumber(rs.getString("호"));
						P3[i].setSemesterCode(rs.getInt("학기"));
						P3[i + 1].setSemesterCode(rs.getInt("학기"));
						P3[i + 2].setSemesterCode(rs.getInt("학기"));
						P3[i + 3].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i += 4)
				{
					if(P4[i].getRoomNumber() == null)
					{
						P4[i].setRoomNumber(rs.getString("호"));
						P4[i + 1].setRoomNumber(rs.getString("호"));
						P4[i + 2].setRoomNumber(rs.getString("호"));
						P4[i + 3].setRoomNumber(rs.getString("호"));
						P4[i].setSemesterCode(rs.getInt("학기"));
						P4[i + 1].setSemesterCode(rs.getInt("학기"));
						P4[i + 2].setSemesterCode(rs.getInt("학기"));
						P4[i + 3].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i += 2)
				{
					if(SN[i].getRoomNumber() == null)
					{
						SN[i].setRoomNumber(rs.getString("호"));
						SN[i + 1].setRoomNumber(rs.getString("호"));
						SN[i].setSemesterCode(rs.getInt("학기"));
						SN[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i += 2)
				{
					if(SY[i].getRoomNumber() == null)
					{
						SY[i].setRoomNumber(rs.getString("호"));
						SY[i + 1].setRoomNumber(rs.getString("호"));
						SY[i].setSemesterCode(rs.getInt("학기"));
						SY[i + 1].setSemesterCode(rs.getInt("학기"));
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//배정내역을 불러와서 서버 메모리에 있는 해당 생활관 호실에 배정내역이 있으면 생활관 호실에 학번을 넣어줌
		String sql1 = "SELECT `호실정보_호`, `자리`, `학생_학번`, `호실정보_생활관명` FROM " + DBHandler.DB_NAME + ".배정내역 WHERE `퇴사예정일` >= '"+ availablePeriod + "'";// 여러 배정내역 (몇년 전꺼까지도) 중에서 아직 쓰고있는 방 예를들어 지금 2학기인데 1학기 1년 입사자
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
			System.out.println(rs1.getString("학생_학번"));
			switch (rs1.getString("호실정보_생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(O1[i].getRoomNumber()) && rs1.getString("자리").equals(O1[i].getSeat()))
					{
						O1[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(O2[i].getRoomNumber()) && rs1.getString("자리").equals(O2[i].getSeat()))
					{
						O2[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(O3[i].getRoomNumber()) && rs1.getString("자리").equals(O3[i].getSeat()))
					{
						O3[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P1[i].getRoomNumber()) && rs1.getString("자리").equals(P1[i].getSeat()))
					{
						P1[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P2[i].getRoomNumber()) && rs1.getString("자리").equals(P2[i].getSeat()))
					{
						P2[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P3[i].getRoomNumber()) && rs1.getString("자리").equals(P3[i].getSeat()))
					{
						P3[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(P4[i].getRoomNumber()) && rs1.getString("자리").equals(P4[i].getSeat()))
					{
						P4[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(SN[i].getRoomNumber()) && rs1.getString("자리").equals(SN[i].getSeat()))
					{
						SN[i].setStudentId(rs1.getString("학생_학번"));
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i++)
				{
					if(rs1.getString("호실정보_호").equals(SY[i].getRoomNumber()) && rs1.getString("자리").equals(SY[i].getSeat()))
					{
						SY[i].setStudentId(rs1.getString("학생_학번"));
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
		while(rs2.next())
		{
			if(arr.contains(rs2.getString("학번")) != true)
			{
				System.out.println(arr.contains(rs2.getString("학번")));
				System.out.println(rs2.getString("학번"));
			switch (rs2.getString("생활관정보_생활관명"))
			{
			case "오름1" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					if(O1[i].getStudentId() == null)
					{
						O1[i].setStudentId(rs2.getString("학번"));
						O1[i].setIsNew(true);
						///////////////////
						System.out.println(checkOutDate1);
						System.out.println(checkOutDate2);
						if(rs2.getInt("지망") == 0)
						{
							O1[i].setCheckout(checkOutDate2);
							break;
						}
						else 
						{
							O1[i].setCheckout(checkOutDate1);
							break;
						}
					}
				}
				break;
			}
			case "오름2" :
			{
				for (int i = 1; i < O2.length; i++)
				{
					if(O2[i].getStudentId() == null)
					{
						O2[i].setStudentId(rs2.getString("학번"));
						O2[i].setIsNew(true);
						if(rs2.getInt("지망") == 0)
						{
							O2[i].setCheckout(checkOutDate2);
							break;
						}
						else 
						{
							O2[i].setCheckout(checkOutDate1);
							break;
						}
					}
				}
				break;
			}
			case "오름3" :
			{
				for (int i = 1; i < O3.length; i++)
				{
					if(O3[i].getStudentId() == null)
					{
						O3[i].setStudentId(rs2.getString("학번"));
						O3[i].setIsNew(true);
						if(rs2.getInt("지망") == 0)
						{
							O3[i].setCheckout(checkOutDate2);
							break;
						}
						else 
						{
							O3[i].setCheckout(checkOutDate1);
							break;
						}
					}
				}
				break;
			}
			case "푸름1" :
			{
				for (int i = 1; i < P1.length; i++)
				{
					if(P1[i].getStudentId() == null)
					{
						if(P1[i].getSeat().equals("B") && P1[i].getRoomNumber().compareTo("500호") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else if(rs2.getInt("지망") == 0)
						{
							P1[i].setCheckout(checkOutDate2);
							P1[i].setStudentId(rs2.getString("학번"));
							P1[i].setIsNew(true);
							break;
						}
						else 
						{
							P1[i].setCheckout(checkOutDate1);
							P1[i].setStudentId(rs2.getString("학번"));
							P1[i].setIsNew(true);
							break;

						}
					}
				}
				break;
			}
			case "푸름2" :
			{
				for (int i = 1; i < P2.length; i++)
				{
					if(P2[i].getStudentId() == null)
					{
						if(P2[i].getSeat().equals("B") && P2[i].getRoomNumber().compareTo("500호") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else if(rs2.getInt("지망") == 0)
						{
							P2[i].setCheckout(checkOutDate2);
							P2[i].setStudentId(rs2.getString("학번"));
							P2[i].setIsNew(true);
							break;

						}
						else 
						{
							P2[i].setCheckout(checkOutDate1);
							P2[i].setStudentId(rs2.getString("학번"));
							P2[i].setIsNew(true);
							break;
						}
					}
				}
				break;
			}
			case "푸름3" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					if(P3[i].getStudentId() == null)
					{
						if((P3[i].getSeat().equals("B") || P3[i].getSeat().equals("D"))&& P3[i].getRoomNumber().compareTo("600호") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else if(rs2.getInt("지망") == 0)
						{
							P3[i].setCheckout(checkOutDate2);
							P3[i].setStudentId(rs2.getString("학번"));
							P3[i].setIsNew(true);
							break;

						}
						else 
						{
							P3[i].setCheckout(checkOutDate1);
							P3[i].setStudentId(rs2.getString("학번"));
							P3[i].setIsNew(true);
							break;
						}
					}
				}
				break;
			}
			case "푸름4" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					if(P4[i].getStudentId() == null)
					{
						if((P4[i].getSeat().equals("B") || P4[i].getSeat().equals("D"))&& P4[i].getRoomNumber().compareTo("600호") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else if(rs2.getInt("지망") == 0)
						{
							P4[i].setCheckout(checkOutDate2);
							P4[i].setStudentId(rs2.getString("학번"));
							P4[i].setIsNew(true);
							break;
						}
						else 
						{
							P4[i].setCheckout(checkOutDate1);
							P4[i].setStudentId(rs2.getString("학번"));
							P4[i].setIsNew(true);
							break;

						}
					}
				}
				break;
			}
			case "신평남" :
			{
				for (int i = 1; i < SN.length; i++)
				{
					if(SN[i].getStudentId() == null)
					{
						SN[i].setStudentId(rs2.getString("학번"));
						SN[i].setIsNew(true);
						if(rs2.getInt("지망") == 0)
						{
							SN[i].setCheckout(checkOutDate2);
							break;
						}
						else 
						{
							SN[i].setCheckout(checkOutDate1);
							break;
						}
					}
				}
				break;
			}
			case "신평여" :
			{
				for (int i = 1; i < SY.length; i++)
				{
					if(SY[i].getStudentId() == null)
					{
						SY[i].setStudentId(rs2.getString("학번"));
						SY[i].setIsNew(true);
						if(rs2.getInt("지망") == 0)
						{
							SY[i].setCheckout(checkOutDate2);
							break;
						}
						else 
						{
							SY[i].setCheckout(checkOutDate1);
							break;
						}
					}
				}
				break;
			}
			case "푸름탑1" :
			{
				for (int i = 1; i < P1.length; i++)
				{
					if(P1[i].getStudentId() == null)
					{
						if(P1[i].getSeat().equals("B") && P1[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							P1[i].setStudentId(rs2.getString("학번"));
							P1[i].setIsNew(true);
							if(rs2.getInt("지망") == 0)
							{
								P1[i].setCheckout(checkOutDate2);
								break;
							}
							else
							{
								P1[i].setCheckout(checkOutDate1);
								break;
							}
						}
						else
						{
																									// 아니면 다시 빈자리로 냅두고 나오라는 뜻
						}
					}
				}
				break;
			}
			case "푸름탑2" :
			{
				for (int i = 1; i < P2.length; i++)
				{
					if(P2[i].getStudentId() == null)
					{
						if(P2[i].getSeat().equals("B") && P2[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							P2[i].setStudentId(rs2.getString("학번"));
							P2[i].setIsNew(true);
							if(rs2.getInt("지망") == 0)
							{
								P2[i].setCheckout(checkOutDate2);
								break;
							}
							else
							{
								P2[i].setCheckout(checkOutDate1);
								break;
							}
						}
						else
						{
																				// 아니면 다시 빈자리로 냅두고 나오라는 뜻
						}
					}
				}
				break;
			}
			case "푸름탑3" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					if(P3[i].getStudentId() == null)
					{
						if((P3[i].getSeat().equals("B") || P3[i].getSeat().equals("D")) && P2[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							P3[i].setStudentId(rs2.getString("학번"));
							P3[i].setIsNew(true);
							if(rs2.getInt("지망") == 0)
							{
								P3[i].setCheckout(checkOutDate2);
								break;
							}
							else
							{
								P3[i].setCheckout(checkOutDate1);
								break;
							}
						}
						else
						{
							P3[i].setStudentId("");													// 아니면 다시 빈자리로 냅두고 나오라는 뜻
						}
					}
				}
				break;
			}
			case "푸름탑4" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					if(P4[i].getStudentId() == null)
					{
						if((P4[i].getSeat().equals("B") || P4[i].getSeat().equals("D")) && P4[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							P4[i].setStudentId(rs2.getString("학번"));
							P4[i].setIsNew(true);
							if(rs2.getInt("지망") == 0)
							{
								P4[i].setCheckout(checkOutDate2);
								break;
							}
							else
							{
								P4[i].setCheckout(checkOutDate1);
								break;
							}
						}
						else
						{
															// 아니면 다시 빈자리로 냅두고 나오라는 뜻
						}
					}
				}
				break;
			}
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//이제 업데이트
		PreparedStatement state3 = connection.prepareStatement(sql);	
		for(int i = 1; i < O1.length; i++)
		{
			if(O1[i].getIsNew() != null && O1[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + O1[i].getStudentId() + "', '" + O1[i].getRoomNumber() + "', '"+ O1[i].getSemesterCode() + "', '오름1', '" + O1[i].getSeat() + "', '" + O1[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < O2.length; i++)
		{
			if(O2[i].getIsNew() != null && O2[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + O2[i].getStudentId() + "', '" + O2[i].getRoomNumber() + "', '"+ O2[i].getSemesterCode() + "', '오름2', '" + O2[i].getSeat() + "', '" + O2[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < O3.length; i++)
		{
			if(O3[i].getIsNew() != null && O3[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + O3[i].getStudentId() + "', '" + O3[i].getRoomNumber() + "', '"+ O3[i].getSemesterCode() + "', '오름3', '" + O3[i].getSeat() + "', '" + O3[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < P1.length; i++)
		{			
			if(P1[i].getIsNew() != null && P1[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P1[i].getStudentId() + "', '" + P1[i].getRoomNumber() + "', '"+ P1[i].getSemesterCode() + "', '푸름1', '" + P1[i].getSeat() + "', '" + P1[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < P2.length; i++)
		{
			if(P2[i].getIsNew() != null && P2[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P2[i].getStudentId() + "', '" + P2[i].getRoomNumber() + "', '"+ P2[i].getSemesterCode() + "', '푸름2', '" + P2[i].getSeat() + "', '" + P2[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < P3.length; i++)
		{
			if(P3[i].getIsNew() != null && P3[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P3[i].getStudentId() + "', '" + P3[i].getRoomNumber() + "', '"+ P3[i].getSemesterCode() + "', '푸름3', '" + P3[i].getSeat() + "', '" + P3[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < P4.length; i++)
		{
			if(P4[i].getIsNew() != null && P4[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + P4[i].getStudentId() + "', '" + P4[i].getRoomNumber() + "', '"+ P4[i].getSemesterCode() + "', '푸름4', '" + P4[i].getSeat() + "', '" + P4[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < SN.length; i++)
		{
			if(SN[i].getIsNew() != null && SN[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + SN[i].getStudentId() + "', '" + SN[i].getRoomNumber() + "', '"+ SN[i].getSemesterCode() + "', '신평남', '" + SN[i].getSeat() + "', '" + SN[i].getCheckOut() +"')";
			state3.executeUpdate(sql);
			}
		}
		for(int i = 1; i < SY.length; i++)
		{
			if(SY[i].getIsNew() != null && SY[i].getIsNew())
			{
			sql = "INSERT INTO " + DBHandler.DB_NAME + ".배정내역 (`학생_학번`, `호실정보_호`, `호실정보_학기`, `호실정보_생활관명`, `자리`, `퇴사예정일`) VALUES ('" + SY[i].getStudentId() + "', '" + SY[i].getRoomNumber() + "', '"+ SY[i].getSemesterCode() + "', '신평여', '" + SY[i].getSeat() + "', '" + SY[i].getCheckOut() +"')";
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
		setAvailablePeriod();
		setCheckOutPeriod();
		residenceUpdate();
	}
}
