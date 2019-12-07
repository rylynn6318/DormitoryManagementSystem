package batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DB.DBinfo;

public class AssignAlgorithm 
{
	public static ResultSet getPassInformation() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT ID, 납부여부, 합격여부, 최종결과 FROM " + DBinfo.DB_NAME + ".신청";
		ResultSet purs = state.executeQuery(sql);
		return purs;
	}
	
	public static ResultSet getConformationState(String studentID) throws SQLException, ClassNotFoundException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT 확인여부 FROM " + DBinfo.DB_NAME + ".서류 WHERE 서류유형 = 1 and 진단일 BETWEEN '19/01/01' and '19/09/01 ' and 학생_ID = " + studentID;
		ResultSet purs = state.executeQuery(sql);
		return purs;
	}
	
	public static void updateFinalPass(String studentID) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + studentID +")";   // 납부여부 Y 결핵 통과 Y 합격여부 Y면 해당 ID의 최종결과 Y 
		state.executeUpdate(sql);
	}
	
	public static void finalPassUpdate() throws SQLException, ClassNotFoundException
	{	
		ResultSet purs = getPassInformation();	//purs 는 지금 id 납부여부 합격여부가 최종결과가 들어있다 신청테이블의
		while(purs.next())			
		{
			boolean document = false; // 유효여부, 진단일, 서류유형이 적합하면 document = true
			ResultSet purs1 = getConformationState(purs.getString("학생_ID"));		//새로운 purs1은 결핵진단서가 통과된 애의 확인 여부를 갖고있다.
			if(purs1.next())
			{
				if(purs1.getString("확인여부").equals("Y"))							//rurs1의 확인여부가 o 즉 결핵 통과이면
				{
					document = true;
				}
				if(purs1.getString("납부여부").equals("Y") && purs1.getString("합격여부").equals("Y") && document == true)		//납부, 합격, 결핵 다 통과면
				{
					updateFinalPass(purs1.getString("학생_ID"));																//학생 ID의 신청테이블 최종결과 Y
				} 
			}
			
		}
		
	}
	
	public static int getCurrentSemester() throws ClassNotFoundException, SQLException
	{
		Statement state1 = DBinfo.connection();
		String sql = "SELECT 학기 FROM " + DBinfo.DB_NAME + ".신청 ORDER BY 학기 DESC LIMIT 1"; //신청테이블에서 하나만 가져와서 그 학기를 봄
		ResultSet rcrs = state1.executeQuery(sql);
		rcrs.next();
		int currentSemester = rcrs.getInt("학기");
		return currentSemester;
	}
	
	public static int getAvailablePeriod() throws SQLException, ClassNotFoundException
	{
		int currentSemester = getCurrentSemester();
		int availablePeriod = 0;
		switch(currentSemester % 10)
		{
		case 1:
			availablePeriod = (currentSemester - 1) * 100 + 620; // 1학기
			break;
		case 2:
			availablePeriod = (currentSemester - 2) * 100 + 720; // 여름계절
			break;
		case 3:
			availablePeriod = (currentSemester - 3) * 100 + 820; // 여름방학 전기간
			break;
		case 4:
			availablePeriod = (currentSemester - 4) * 100 + 1220; // 2학기
			break;
		case 5:
			availablePeriod = (currentSemester - 5) * 100 + 10120; // 겨울계절
			break;
		case 6:
			availablePeriod = (currentSemester - 6) * 100 + 10220; // 겨울방학 전기간
			break;
		}
		return availablePeriod;
	}
	
	public static int getCheckOutDate(int choice, int semester) throws ClassNotFoundException, SQLException
	{
		int checkOutDate = 0;
		if(choice == 0)													// 0지망 이면
		{
			checkOutDate = ((semester / 10) * 1000 + 1220);			// 1년입사의 퇴사일
		}
		else
		{
			switch(getCurrentSemester() % 10)
			{
			case 1:
				checkOutDate = (semester - 1) * 100 + 620;	// 1학기 
				break;
			case 2:
				checkOutDate = (semester - 2) * 100 + 720;	// 여름계절
				break;
			case 3:
				checkOutDate = (semester - 3) * 100 + 820;	// 여름 전기간
				break;
			case 4:
				checkOutDate = (semester - 4) * 100 + 1220;	// 2학기
				break;
			case 5:
				checkOutDate = (semester - 5) * 100 + 10120; //	겨울계절
				break;
			case 6:
				checkOutDate = (semester - 6) * 100 + 10220; // 겨울 전기간
				break;
			}
		}
		return checkOutDate;
	}

		
				
				
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static ResultSet getRoomInfo() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT 호, 생활관명, 학기 FROM " + DBinfo.DB_NAME + ".호실정보";
		ResultSet rurs = state.executeQuery(sql);
		return rurs;
	}
	
	public static ResultSet getRoomBoarder() throws ClassNotFoundException, SQLException
	{
		String sql = "SELECT 호실정보_호, 자리, 학생_ID FROM " + DBinfo.DB_NAME + ".배정내역 WHERE 퇴사예정일 > "+ getAvailablePeriod();// 여러 배정내역 (몇년 전꺼까지도) 중에서 아직 쓰고있는 방 예를들어 지금 2학기인데 1학기 1년 입사자
		//호 옆에 생활관명 넣어야함
		Statement state = DBinfo.connection();
		ResultSet rurs = state.executeQuery(sql);
		return rurs;
	}
	
	public static ResultSet getFinalPass() throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "SELECT ID, 생활관명, 지망, 학기 FROM " + DBinfo.DB_NAME + ".신청 WHERE 최종결과 = 'Y' order by 생활관명, 코골이여부";  // 최종결과가 Y인 신청에 대해 정보를 가져옴
		ResultSet rurs = state.executeQuery(sql);
		return rurs;
	}
	
	public static void updateAssignInfo (String studentID, String seat, int checkOut, int semesterCode, String roomNumber) throws ClassNotFoundException, SQLException
	{
		Statement state = DBinfo.connection();
		String sql = "INSERT INTO " + DBinfo.DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + studentID + "', '" + seat+ "', '" + checkOut + "', '오름1', '" + semesterCode + "', '" + roomNumber +"')";
		state.executeUpdate(sql);
	}
	
	public static void residenceUpdate() throws ClassNotFoundException, SQLException
	{
		ResultSet rurs = getRoomInfo();
		AssignRoomInfo[] O1 = MakeAllRoomInfo.getO1();
		AssignRoomInfo[] O2 = MakeAllRoomInfo.getO2();
		AssignRoomInfo[] O3 = MakeAllRoomInfo.getO3();
		AssignRoomInfo[] P1 = MakeAllRoomInfo.getP1();
		AssignRoomInfo[] P2 = MakeAllRoomInfo.getP2();
		AssignRoomInfo[] P3 = MakeAllRoomInfo.getP3();
		AssignRoomInfo[] P4 = MakeAllRoomInfo.getP4();
		AssignRoomInfo[] SN = MakeAllRoomInfo.getSN();
		AssignRoomInfo[] SY = MakeAllRoomInfo.getSY();
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// DB에 있는 생활관 호실 정보를 가져와서 서버 메모리 에 올림
		while(rurs.next())
		{
			switch (rurs.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i += 2)
				{
					if(O1[i].getRoomNumber() == null)
					{
						O1[i].setRoomNumber(rurs.getString("호"));
						O1[i + 1].setRoomNumber(rurs.getString("호"));
						O1[i].setSemesterCode(rurs.getInt("학기"));
						O1[i + 1].setSemesterCode(rurs.getInt("학기"));
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
						O2[i].setRoomNumber(rurs.getString("호"));
						O2[i + 1].setRoomNumber(rurs.getString("호"));
						O2[i].setSemesterCode(rurs.getInt("학기"));
						O2[i + 1].setSemesterCode(rurs.getInt("학기"));
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
						O3[i].setRoomNumber(rurs.getString("호"));
						O3[i + 1].setRoomNumber(rurs.getString("호"));
						O3[i].setSemesterCode(rurs.getInt("학기"));
						O3[i + 1].setSemesterCode(rurs.getInt("학기"));
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
						P1[i].setRoomNumber(rurs.getString("호"));
						P1[i + 1].setRoomNumber(rurs.getString("호"));
						P1[i].setSemesterCode(rurs.getInt("학기"));
						P1[i + 1].setSemesterCode(rurs.getInt("학기"));
					}
				}
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i += 2)
				{
					if(P2[i].getRoomNumber() == null)
					{
						P2[i].setRoomNumber(rurs.getString("호"));
						P2[i + 1].setRoomNumber(rurs.getString("호"));
						P2[i].setSemesterCode(rurs.getInt("학기"));
						P2[i + 1].setSemesterCode(rurs.getInt("학기"));
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
						P3[i].setRoomNumber(rurs.getString("호"));
						P3[i + 1].setRoomNumber(rurs.getString("호"));
						P3[i + 2].setRoomNumber(rurs.getString("호"));
						P3[i + 3].setRoomNumber(rurs.getString("호"));
						P3[i].setSemesterCode(rurs.getInt("학기"));
						P3[i + 1].setSemesterCode(rurs.getInt("학기"));
						P3[i + 2].setSemesterCode(rurs.getInt("학기"));
						P3[i + 3].setSemesterCode(rurs.getInt("학기"));
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
						P4[i].setRoomNumber(rurs.getString("호"));
						P4[i + 1].setRoomNumber(rurs.getString("호"));
						P4[i + 2].setRoomNumber(rurs.getString("호"));
						P4[i + 3].setRoomNumber(rurs.getString("호"));
						P4[i].setSemesterCode(rurs.getInt("학기"));
						P4[i + 1].setSemesterCode(rurs.getInt("학기"));
						P4[i + 2].setSemesterCode(rurs.getInt("학기"));
						P4[i + 3].setSemesterCode(rurs.getInt("학기"));
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
						SN[i].setRoomNumber(rurs.getString("호"));
						SN[i + 1].setRoomNumber(rurs.getString("호"));
						SN[i].setSemesterCode(rurs.getInt("학기"));
						SN[i + 1].setSemesterCode(rurs.getInt("학기"));
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
						SY[i].setRoomNumber(rurs.getString("호"));
						SY[i + 1].setRoomNumber(rurs.getString("호"));
						SY[i].setSemesterCode(rurs.getInt("학기"));
						SY[i + 1].setSemesterCode(rurs.getInt("학기"));
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//배정내역을 불러와서 서버 메모리에 있는 해당 생활관 호실에 배정내역이 있으면 생활관 호실에 학번을 넣어줌
		
		ResultSet rurs1 = getRoomBoarder();
		while(rurs1.next())
		{
			switch (rurs1.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i++)
				{
					if(rurs1.getString("호").equals(O1[i].getRoomNumber()) && rurs1.getString("자리").equals(O1[i].getSeat()))
					{
						O1[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i++)
				{
					if(rurs1.getString("호").equals(O2[i].getRoomNumber()) && rurs1.getString("자리").equals(O2[i].getSeat()))
					{
						O2[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i++)
				{
					if(rurs1.getString("호").equals(O3[i].getRoomNumber()) && rurs1.getString("자리").equals(O3[i].getSeat()))
					{
						O3[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i++)
				{
					if(rurs1.getString("호").equals(P1[i].getRoomNumber()) && rurs1.getString("자리").equals(P1[i].getSeat()))
					{
						P1[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i++)
				{
					if(rurs1.getString("호").equals(P2[i].getRoomNumber()) && rurs1.getString("자리").equals(P2[i].getSeat()))
					{
						P2[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i++)
				{
					if(rurs1.getString("호").equals(P3[i].getRoomNumber()) && rurs1.getString("자리").equals(P3[i].getSeat()))
					{
						P3[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i++)
				{
					if(rurs1.getString("호").equals(P4[i].getRoomNumber()) && rurs1.getString("자리").equals(P4[i].getSeat()))
					{
						P4[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i++)
				{
					if(rurs1.getString("호").equals(SN[i].getRoomNumber()) && rurs1.getString("자리").equals(SN[i].getSeat()))
					{
						SN[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i++)
				{
					if(rurs1.getString("호").equals(SY[i].getRoomNumber()) && rurs1.getString("자리").equals(SY[i].getSeat()))
					{
						SY[i].setStudentId(rurs1.getString("학생_ID"));
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//신청자가 신청한 생활관을 가져와서 메모리에 있는 현재 배정된 내역과 대조하면서 방에 넣어줌
		ResultSet rurs2 = getFinalPass();
		
		while(rurs2.next())
		{
			switch (rurs2.getString("생활관명"))
			{
			case "오름1" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					if(O1[i].getStudentId() == null)
					{
						O1[i].setStudentId(rurs2.getString("ID"));
						O1[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));	
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
						O2[i].setStudentId(rurs2.getString("ID"));
						O2[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));	
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
						O3[i].setStudentId(rurs2.getString("ID"));
						O3[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));	
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
						if(P1[i].getSeat().equals("B") && P1[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else
						{
							P1[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P1[i].setStudentId(rurs2.getString("ID"));
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
						if(P2[i].getSeat().equals("B") && P2[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else
						{
							P2[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P2[i].setStudentId(rurs2.getString("ID"));
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
						if((P3[i].getSeat().equals("B") || P3[i].getSeat().equals("D"))&& P3[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else 
						{
							P3[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P3[i].setStudentId(rurs2.getString("ID"));
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
						if((P4[i].getSeat().equals("B") || P4[i].getSeat().equals("D"))&& P4[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
						}
						else 
						{
							P4[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P4[i].setStudentId(rurs2.getString("ID"));
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
						SN[i].setStudentId(rurs2.getString("ID"));
						SN[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
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
						SY[i].setStudentId(rurs2.getString("ID"));
						SY[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
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
							P1[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P1[i].setStudentId(rurs2.getString("ID"));
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
							P2[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P2[i].setStudentId(rurs2.getString("ID"));
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
							P3[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P3[i].setStudentId(rurs2.getString("ID"));
						}
						else
						{
																	// 아니면 다시 빈자리로 냅두고 나오라는 뜻
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
							P4[i].setCheckout(getCheckOutDate(rurs2.getInt("지망"), rurs2.getInt("학기")));
							P4[i].setStudentId(rurs2.getString("ID"));
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
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//이제 업데이트
		for(int i = 1; i < O1.length; i++)
		{
			updateAssignInfo(O1[i].getStudentId(), O1[i].getSeat(), O1[i].getCheckOut(), O1[i].getSemesterCode(),O1[i].getRoomNumber());
		}
		for(int i = 1; i < O2.length; i++)
		{
			updateAssignInfo(O2[i].getStudentId(), O2[i].getSeat(), O2[i].getCheckOut(), O2[i].getSemesterCode(),O2[i].getRoomNumber());
		}
		for(int i = 1; i < O3.length; i++)
		{
			updateAssignInfo(O3[i].getStudentId(), O3[i].getSeat(), O3[i].getCheckOut(), O3[i].getSemesterCode(),O3[i].getRoomNumber());
		}
		for(int i = 1; i < P1.length; i++)
		{
			updateAssignInfo(P1[i].getStudentId(), P1[i].getSeat(), P1[i].getCheckOut(), P1[i].getSemesterCode(),P1[i].getRoomNumber());
		}
		for(int i = 1; i < P2.length; i++)
		{
			updateAssignInfo(P2[i].getStudentId(), P2[i].getSeat(), P2[i].getCheckOut(), P2[i].getSemesterCode(),P2[i].getRoomNumber());
		}
		for(int i = 1; i < P3.length; i++)
		{
			updateAssignInfo(P3[i].getStudentId(), P3[i].getSeat(), P3[i].getCheckOut(), P3[i].getSemesterCode(),P3[i].getRoomNumber());
		}
		for(int i = 1; i < P4.length; i++)
		{
			updateAssignInfo(P4[i].getStudentId(), P4[i].getSeat(), P4[i].getCheckOut(), P4[i].getSemesterCode(),P4[i].getRoomNumber());
		}
		for(int i = 1; i < SN.length; i++)
		{
			updateAssignInfo(SN[i].getStudentId(), SN[i].getSeat(), SN[i].getCheckOut(), SN[i].getSemesterCode(),SN[i].getRoomNumber());
		}
		for(int i = 1; i < SY.length; i++)
		{
			updateAssignInfo(SY[i].getStudentId(), SY[i].getSeat(), SY[i].getCheckOut(), SY[i].getSemesterCode(),SY[i].getRoomNumber());
		}
	}
	public static void batchStart() throws ClassNotFoundException, SQLException
	{
		finalPassUpdate();
		residenceUpdate();
	}
}

