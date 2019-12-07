package batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import DB.ConformationState;
import DB.CurrentSemesterParser;
import DB.FinalPassParser;
import DB.PassInformationParser;
import DB.RoomBoarderParser;
import DB.RoomInfoParser;
import DB.UpdateAssignInfo;
import DB.UpdateFinalPassParser;

public class AssignAlgorithm 
{		
		
	public static void finalPassUpdate() throws SQLException, ClassNotFoundException
	{	
		ResultSet purs = PassInformationParser.getPassInformation();	//purs 는 지금 id 납부여부 합격여부가 최종결과가 들어있다 신청테이블의
		while(purs.next())			
		{
			boolean document = false; // 유효여부, 진단일, 서류유형이 적합하면 document = true
			ResultSet purs1 = ConformationState.getConformationState(purs.getString("학생_ID"));		//새로운 purs1은 결핵진단서가 통과된 애의 확인 여부를 갖고있다.
			if(purs1.next())
			{
				if(purs1.getString("확인여부").equals("Y"))							//rurs1의 확인여부가 o 즉 결핵 통과이면
				{
					document = true;
				}
				if(purs1.getString("납부여부").equals("Y") && purs1.getString("합격여부").equals("Y") && document == true)		//납부, 합격, 결핵 다 통과면
				{
					UpdateFinalPassParser.updateFinalPass(purs1.getString("학생_ID"));																//학생 ID의 신청테이블 최종결과 Y
				} 
			}
			
		}
		
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
			switch(CurrentSemesterParser.getCurrentSemester() % 10)
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
	
	public static void residenceUpdate() throws ClassNotFoundException, SQLException
	{
		ResultSet rurs = RoomInfoParser.getRoomInfo();
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
		
		ResultSet rurs1 = RoomBoarderParser.getRoomBoarder();
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
		
		ResultSet rurs2 = FinalPassParser.getFinalPass();
		
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
			UpdateAssignInfo.updateAssignInfo(O1[i].getStudentId(), O1[i].getSeat(), O1[i].getCheckOut(), O1[i].getSemesterCode(),O1[i].getRoomNumber());
		}
		for(int i = 1; i < O2.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(O2[i].getStudentId(), O2[i].getSeat(), O2[i].getCheckOut(), O2[i].getSemesterCode(),O2[i].getRoomNumber());
		}
		for(int i = 1; i < O3.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(O3[i].getStudentId(), O3[i].getSeat(), O3[i].getCheckOut(), O3[i].getSemesterCode(),O3[i].getRoomNumber());
		}
		for(int i = 1; i < P1.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(P1[i].getStudentId(), P1[i].getSeat(), P1[i].getCheckOut(), P1[i].getSemesterCode(),P1[i].getRoomNumber());
		}
		for(int i = 1; i < P2.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(P2[i].getStudentId(), P2[i].getSeat(), P2[i].getCheckOut(), P2[i].getSemesterCode(),P2[i].getRoomNumber());
		}
		for(int i = 1; i < P3.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(P3[i].getStudentId(), P3[i].getSeat(), P3[i].getCheckOut(), P3[i].getSemesterCode(),P3[i].getRoomNumber());
		}
		for(int i = 1; i < P4.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(P4[i].getStudentId(), P4[i].getSeat(), P4[i].getCheckOut(), P4[i].getSemesterCode(),P4[i].getRoomNumber());
		}
		for(int i = 1; i < SN.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(SN[i].getStudentId(), SN[i].getSeat(), SN[i].getCheckOut(), SN[i].getSemesterCode(),SN[i].getRoomNumber());
		}
		for(int i = 1; i < SY.length; i++)
		{
			UpdateAssignInfo.updateAssignInfo(SY[i].getStudentId(), SY[i].getSeat(), SY[i].getCheckOut(), SY[i].getSemesterCode(),SY[i].getRoomNumber());
		}
	}
	
	public static void batchStart() throws ClassNotFoundException, SQLException
	{
		finalPassUpdate();
		residenceUpdate();
	}
}

