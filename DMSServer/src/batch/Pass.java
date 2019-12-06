package batch;

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
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	private static int currentSemester;
	private static int availablePeriod;
	private static int checkOutDate1; //이건 1학기나 계절학기 신청자들의 기숙사 종료일 어차피 현재 받고있는 신청자들의 학기에 따라 변수 알아서 바뀌니 걱정 ㄴ
	private static int checkOutDate2; //이건 1년 신청자들의 기숙사 종료일
	
	public static void passUpdate() throws SQLException, ClassNotFoundException
	{	
		
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		Statement state1 = conn.createStatement();
		Statement state2 = conn.createStatement();
		String sql = "SELECT ID, 납부여부, 합격여부, 최종결과 FROM " + DB_NAME + ".신청";
		ResultSet purs = state.executeQuery(sql);
		while(purs.next())
		{
			boolean document = false; // 유효여부, 진단일, 서류유형이 적합하면 document = true
			String sql1 = "SELECT 확인여부 FROM " + DB_NAME + ".서류 WHERE 서류유형 = 1 and 진단일 BETWEEN '19/01/01' and '19/09/01 ' and 학생_ID = " + purs.getString("ID");
			ResultSet purs2 = state1.executeQuery(sql1);

			if(purs2.next())
			{
				if(purs2.getString("확인여부").equals("Y"))
				{
					document = true;
				}
				if(purs.getString("납부여부").equals("Y") && purs.getString("합격여부").equals("Y") && document == true)
				{
					String sql2 = "update Prototype.신청  set 최종결과 = 'Y' WHERE (ID = " + purs.getString("학생_ID")+")";   // 납부여부 Y 결핵 통과 Y 합격여부 Y면 해당 ID의 최종결과 Y 
					state2.executeUpdate(sql2);
				} 
			}
			
		}
		
	}
	
	public static void setCurrentSemester() throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state3 = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state3 = conn.createStatement();
		
		String sql = "SELECT 학기 FROM " + DB_NAME + ".신청 ORDER BY 학기 DESC LIMIT 1"; //신청테이블에서 하나만 가져와서 그 학기를 봄
		ResultSet rcrs = state3.executeQuery(sql);
		rcrs.next();
		currentSemester = rcrs.getInt("학기");
	}
	
	public static void setAvailablePeriod()
	{
		switch(currentSemester % 10)
		{
		case 1:
			availablePeriod = (currentSemester - 1) * 100 + 620; // 1학기
			break;
		case 2:
			availablePeriod = (currentSemester - 2) * 100 + 720; // 여름계절
			break;
		case 3:
			availablePeriod = (currentSemester - 3) * 100 + 1220; // 2학기
			break;
		case 4:
			availablePeriod = (currentSemester - 4) * 100 + 10120; // 겨울계절
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
			checkOutDate1 = (currentSemester - 2) * 100 + 720;	// 여름계절
			break;
		case 3:
			checkOutDate1 = (currentSemester - 3) * 100 + 1220;	// 2학기
			break;
		case 4:
			checkOutDate1 = (currentSemester - 4) * 100 + 10120; //	겨울계절
			break;
		}
	}

		
				
				//여기에요 여기 여기부터 코딩 하시면 돼요
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		String sql2 = "SELECT 호, 생활관명 FROM " + DB_NAME + ".호실정보";
		ResultSet rurs3 = state.executeQuery(sql2);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// DB에 있는 생활관 호실 정보를 가져와서 서버 메모리 에 올림
		while(rurs3.next())
		{
			switch (rurs3.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i += 2)
				{
					if(O1[i].getRoomNumber() == "")
					{
						O1[i].setRoomNumber(rurs3.getString("호"));
						O1[i + 1].setRoomNumber(rurs3.getString("호"));
						O1[i].setSemesterCode(currentSemester);
						O1[i + 1].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i += 2)
				{
					if(O2[i].getRoomNumber() == "")
					{
						O2[i].setRoomNumber(rurs3.getString("호"));
						O2[i + 1].setRoomNumber(rurs3.getString("호"));
						O2[i].setSemesterCode(currentSemester);
						O2[i + 1].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i += 2)
				{
					if(O3[i].getRoomNumber() == "")
					{
						O3[i].setRoomNumber(rurs3.getString("호"));
						O3[i + 1].setRoomNumber(rurs3.getString("호"));
						O3[i].setSemesterCode(currentSemester);
						O3[i + 1].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i += 2)
				{
					if(P1[i].getRoomNumber() == "")
					{
						P1[i].setRoomNumber(rurs3.getString("호"));
						P1[i + 1].setRoomNumber(rurs3.getString("호"));
						P1[i].setSemesterCode(currentSemester);
						P1[i + 1].setSemesterCode(currentSemester);
					}
				}
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i += 2)
				{
					if(P2[i].getRoomNumber() == "")
					{
						P2[i].setRoomNumber(rurs3.getString("호"));
						P2[i + 1].setRoomNumber(rurs3.getString("호"));
						P2[i].setSemesterCode(currentSemester);
						P2[i + 1].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i += 4)
				{
					if(P3[i].getRoomNumber() == "")
					{
						P3[i].setRoomNumber(rurs3.getString("호"));
						P3[i + 1].setRoomNumber(rurs3.getString("호"));
						P3[i + 2].setRoomNumber(rurs3.getString("호"));
						P3[i + 3].setRoomNumber(rurs3.getString("호"));
						P3[i].setSemesterCode(currentSemester);
						P3[i + 1].setSemesterCode(currentSemester);
						P3[i + 2].setSemesterCode(currentSemester);
						P3[i + 3].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i += 4)
				{
					if(P4[i].getRoomNumber() == "")
					{
						P4[i].setRoomNumber(rurs3.getString("호"));
						P4[i + 1].setRoomNumber(rurs3.getString("호"));
						P4[i + 2].setRoomNumber(rurs3.getString("호"));
						P4[i + 3].setRoomNumber(rurs3.getString("호"));
						P4[i].setSemesterCode(currentSemester);
						P4[i + 1].setSemesterCode(currentSemester);
						P3[i + 2].setSemesterCode(currentSemester);
						P3[i + 3].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i += 2)
				{
					if(SN[i].getRoomNumber() == "")
					{
						SN[i].setRoomNumber(rurs3.getString("호"));
						SN[i + 1].setRoomNumber(rurs3.getString("호"));
						SN[i].setSemesterCode(currentSemester);
						SN[i + 1].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i += 2)
				{
					if(SY[i].getRoomNumber() == "")
					{
						SY[i].setRoomNumber(rurs3.getString("호"));
						SY[i + 1].setRoomNumber(rurs3.getString("호"));
						SY[i].setSemesterCode(currentSemester);
						SY[i + 1].setSemesterCode(currentSemester);
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//배정내역을 불러와서 서버 메모리에 있는 해당 생활관 호실에 배정내역이 있으면 생활관 호실에 학번을 넣어줌
		String sql3 = "SELECT 호실정보_호, 생활관명, 자리, ID FROM " + DB_NAME + ".배정내역 WHERE 퇴사예정일 > "+ availablePeriod;// 여러 배정내역 (몇년 전꺼까지도) 중에서 아직 쓰고있는 방 예를들어 지금 2학기인데 1학기 1년 입사자
		Statement state4 = conn.createStatement();
		ResultSet rurs4 = state4.executeQuery(sql3);
		
		while(rurs4.next())
		{
			switch (rurs4.getString("생활관명"))
			{
			case "오름1" :
			{
				for(int i = 1; i < O1.length; i++)
				{
					if(rurs4.getString("호") == O1[i].getRoomNumber() && rurs4.getString("자리") == O1[i].getSeat())
					{
						O1[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "오름2" :
			{
				for(int i = 1; i < O2.length; i++)
				{
					if(rurs4.getString("호") == O2[i].getRoomNumber() && rurs4.getString("자리") == O2[i].getSeat())
					{
						O2[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "오름3" :
			{
				for(int i = 1; i < O3.length; i++)
				{
					if(rurs4.getString("호") == O3[i].getRoomNumber() && rurs4.getString("자리") == O3[i].getSeat())
					{
						O3[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "푸름1" :
			{
				for(int i = 1; i < P1.length; i++)
				{
					if(rurs4.getString("호") == P1[i].getRoomNumber() && rurs4.getString("자리") == P1[i].getSeat())
					{
						P1[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "푸름2" :
			{
				for(int i = 1; i < P2.length; i++)
				{
					if(rurs4.getString("호") == P2[i].getRoomNumber() && rurs4.getString("자리") == P2[i].getSeat())
					{
						P2[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "푸름3" :
			{
				for(int i = 1; i < P3.length; i++)
				{
					if(rurs4.getString("호") == P3[i].getRoomNumber() && rurs4.getString("자리") == P3[i].getSeat())
					{
						P3[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "푸름4" :
			{
				for(int i = 1; i < P4.length; i++)
				{
					if(rurs4.getString("호") == P4[i].getRoomNumber() && rurs4.getString("자리") == P4[i].getSeat())
					{
						P4[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "신평남" :
			{
				for(int i = 1; i < SN.length; i++)
				{
					if(rurs4.getString("호") == SN[i].getRoomNumber() && rurs4.getString("자리") == SN[i].getSeat())
					{
						SN[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			case "신평여" :
			{
				for(int i = 1; i < SY.length; i++)
				{
					if(rurs4.getString("호") == SY[i].getRoomNumber() && rurs4.getString("자리") == SY[i].getSeat())
					{
						SY[i].setStudentId(rurs4.getString("ID"));
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//신청자가 신청한 생활관을 가져와서 메모리에 있는 현재 배정된 내역과 대조하면서 방에 넣어줌
		
		Statement state5 = conn.createStatement();
		String sql5 = "SELECT ID, 생활관명 FROM 지망 " + DB_NAME + ".신청 WHERE (퇴사예정일> "+ availablePeriod +" and 최종결과 = 'Y') order by 생활관명";  //이미 누가 쓰고 있는방인가 알기위한 쿼리
		ResultSet rurs5 = state5.executeQuery(sql5);
		
		while(rurs5.next())
		{
			switch (rurs5.getString("생활관명"))
			{
			case "오름1" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					if(O1[i].getStudentId() == "")
					{
						O1[i].setStudentId(rurs5.getString("ID"));
						if(rurs5.getInt("지망") == 0)
						{
							O1[i].setCheckout(checkOutDate2);
						}
						else 
						{
							O1[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "오름2" :
			{
				for (int i = 1; i < O2.length; i++)
				{
					if(O2[i].getStudentId() == "")
					{
						O2[i].setStudentId(rurs5.getString("ID"));
						if(rurs5.getInt("지망") == 0)
						{
							O2[i].setCheckout(checkOutDate2);
						}
						else 
						{
							O2[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "오름3" :
			{
				for (int i = 1; i < O3.length; i++)
				{
					if(O3[i].getStudentId() == "")
					{
						O3[i].setStudentId(rurs5.getString("ID"));
						if(rurs5.getInt("지망") == 0)
						{
							O3[i].setCheckout(checkOutDate2);
						}
						else 
						{
							O3[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "푸름1" :
			{
				for (int i = 1; i < P1.length; i++)
				{
					if(P1[i].getStudentId() == "")
					{
						P1[i].setStudentId(rurs5.getString("ID"));
						if(P1[i].getSeat().equals("B") && P1[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
							P1[i].setStudentId("");
						}
						else if(rurs5.getInt("지망") == 0)
						{
							P1[i].setCheckout(checkOutDate2);
						}
						else 
						{
							P1[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "푸름2" :
			{
				for (int i = 1; i < P2.length; i++)
				{
					if(P2[i].getStudentId() == "")
					{
						P2[i].setStudentId(rurs5.getString("ID"));
						if(P2[i].getSeat().equals("B") && P2[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣지마라는 뜻
						{
							P2[i].setStudentId("");
						}
						else if(rurs5.getInt("지망") == 0)
						{
							P2[i].setCheckout(checkOutDate2);
						}
						else 
						{
							P2[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "푸름3" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					P3[i].setStudentId(rurs5.getString("ID"));
					if((P3[i].getSeat().equals("B") || P3[i].getSeat().equals("D"))&& P3[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣지마라는 뜻
					{
						P3[i].setStudentId("");
					}
					else if(rurs5.getInt("지망") == 0)
					{
						P3[i].setCheckout(checkOutDate2);
					}
					else 
					{
						P3[i].setCheckout(checkOutDate1);
					}
				}
				break;
			}
			case "푸름4" :
			{
				for (int i = 1; i < O1.length; i++)
				{
					P3[i].setStudentId(rurs5.getString("ID"));
					if((P3[i].getSeat().equals("B") || P3[i].getSeat().equals("D"))&& P3[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣지마라는 뜻
					{
						P3[i].setStudentId("");
					}
					else if(rurs5.getInt("지망") == 0)
					{
						P3[i].setCheckout(checkOutDate2);
					}
					else 
					{
						P3[i].setCheckout(checkOutDate1);
					}
				}
				break;
			}
			case "신평남" :
			{
				for (int i = 1; i < SN.length; i++)
				{
					if(SN[i].getStudentId() == "")
					{
						SN[i].setStudentId(rurs5.getString("ID"));
						if(rurs5.getInt("지망") == 0)
						{
							SN[i].setCheckout(checkOutDate2);
						}
						else 
						{
							SN[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "신평여" :
			{
				for (int i = 1; i < SY.length; i++)
				{
					if(SY[i].getStudentId() == "")
					{
						SY[i].setStudentId(rurs5.getString("ID"));
						if(rurs5.getInt("지망") == 0)
						{
							SY[i].setCheckout(checkOutDate2);
						}
						else 
						{
							SY[i].setCheckout(checkOutDate1);
						}
					}
				}
				break;
			}
			case "푸름탑1" :
			{
				for (int i = 1; i < P1.length; i++)
				{
					if(P1[i].getStudentId() == "")
					{
						P1[i].setStudentId(rurs5.getString("ID"));
						if(P1[i].getSeat().equals("B") && P1[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							if(rurs5.getInt("지망") == 0)
							{
								P1[i].setCheckout(checkOutDate2);
							}
							else
							{
								P1[i].setCheckout(checkOutDate1);
							}
						}
						else
						{
							P1[i].setStudentId("");													// 아니면 다시 빈자리로 냅두고 나와
						}
					}
				}
				break;
			}
			case "푸름탑2" :
			{
				for (int i = 1; i < P2.length; i++)
				{
					if(P2[i].getStudentId() == "")
					{
						P2[i].setStudentId(rurs5.getString("ID"));
						if(P2[i].getSeat().equals("B") && P2[i].getRoomNumber().compareTo("500") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							if(rurs5.getInt("지망") == 0)
							{
								P2[i].setCheckout(checkOutDate2);
							}
							else
							{
								P2[i].setCheckout(checkOutDate1);
							}
						}
						else
						{
							P2[i].setStudentId("");													// 아니면 다시 빈자리로 냅두고 나와
						}
					}
				}
				break;
			}
			case "푸름탑3" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					if(P3[i].getStudentId() == "")
					{
						P3[i].setStudentId(rurs5.getString("ID"));
						if((P3[i].getSeat().equals("B") || P3[i].getSeat().equals("D")) && P2[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							if(rurs5.getInt("지망") == 0)
							{
								P3[i].setCheckout(checkOutDate2);
							}
							else
							{
								P3[i].setCheckout(checkOutDate1);
							}
						}
						else
						{
							P3[i].setStudentId("");													// 아니면 다시 빈자리로 냅두고 나와
						}
					}
				}
				break;
			}
			case "푸름탑4" :
			{
				for (int i = 1; i < P3.length; i++)
				{
					if(P4[i].getStudentId() == "")
					{
						P4[i].setStudentId(rurs5.getString("ID"));
						if((P4[i].getSeat().equals("B") || P4[i].getSeat().equals("D")) && P4[i].getRoomNumber().compareTo("600") > 0) // 탑층 자리이면 넣으라는 뜻
						{
							if(rurs5.getInt("지망") == 0)
							{
								P4[i].setCheckout(checkOutDate2);
							}
							else
							{
								P4[i].setCheckout(checkOutDate1);
							}
						}
						else
						{
							P4[i].setStudentId("");													// 아니면 다시 빈자리로 냅두고 나와
						}
					}
				}
				break;
			}
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//이제 업데이트
		Statement state6 = conn.createStatement();
		String sql6;
		for(int i = 1; i < O1.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + O1[i].getStudentId() + "', '" + O1[i].getSeat()+ "', '"+O1[i].getCheckOut()+ "', '오름1', '" + O1[i].getSemesterCode() + "', '" + O1[i].getRoomNumber() +"')";
			state6.executeQuery(sql6);
		}
		for(int i = 1; i < O2.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + O2[i].getStudentId() + "', '" + O2[i].getSeat()+ "', '"+O2[i].getCheckOut()+ "', '오름2', '" + O2[i].getSemesterCode() + "', '" + O2[i].getRoomNumber() +"')";
			state6.executeQuery(sql6);
		}
		for(int i = 1; i < O3.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + O3[i].getStudentId() + "', '" + O3[i].getSeat()+ "', '"+O3[i].getCheckOut()+ "', '오름3', '" + O3[i].getSemesterCode() + "', '" + O3[i].getRoomNumber() +"')";
			state6.executeQuery(sql6);
		}
		for(int i = 1; i < P1.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P1[i].getStudentId() + "', '" + P1[i].getSeat()+ "', '"+P1[i].getCheckOut()+ "', '푸름1', '" + P1[i].getSemesterCode() + "', '" + P1[i].getRoomNumber() +"')";
			state6.executeQuery(sql6);
		}
		for(int i = 1; i < P2.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P2[i].getStudentId() + "', '" + P2[i].getSeat()+ "', '"+P2[i].getCheckOut()+ "', '푸름2', '" + P2[i].getSemesterCode() + "', '" + P2[i].getRoomNumber() +"')";
			state6.executeQuery(sql6);
		}
		for(int i = 1; i < P3.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P3[i].getStudentId() + "', '" + P3[i].getSeat()+ "', '"+P3[i].getCheckOut()+ "', '푸름3', '" + P3[i].getSemesterCode() + "', '" + P3[i].getRoomNumber() +"')";
			state6.executeQuery(sql6);
		}
		for(int i = 1; i < P4.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + P4[i].getStudentId() + "', '" + P4[i].getSeat()+ "', '"+P4[i].getCheckOut()+ "', '푸름4', '" + P4[i].getSemesterCode() + "', '" + P4[i].getRoomNumber() +"')";
			state.executeQuery(sql6);
		}
		for(int i = 1; i < SN.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + SN[i].getStudentId() + "', '" + SN[i].getSeat()+ "', '"+SN[i].getCheckOut()+ "', '신평남', '" + SN[i].getSemesterCode() + "', '" + SN[i].getRoomNumber() +"')";
			state.executeQuery(sql6);
		}
		for(int i = 1; i < SY.length; i++)
		{
			sql6 = "INSERT INTO " + DB_NAME + ".배정내역' ('학번', '자리', '퇴사예정일', '호실정보_생활관명', '호실정보_학기', '호실정보_호') VALUES ('" + SY[i].getStudentId() + "', '" + SY[i].getSeat()+ "', '"+SY[i].getCheckOut()+ "', '신평여', '" + SY[i].getSemesterCode() + "', '" + SY[i].getRoomNumber() +"')";
			state.executeQuery(sql6);
		}
		
		
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		setCurrentSemester();
		setAvailablePeriod();
		setCheckOutPeriod();
		passUpdate();
		residenceUpdate();
	}
}

