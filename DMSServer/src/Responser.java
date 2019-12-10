import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import DB.ApplicationParser;
import DB.AssignAlgorithm;
import DB.CurrentSemesterParser;
import DB.DocumentParser;
import DB.DormParser;
import DB.PlacementHistoryParser;
import DB.ScheduleParser;
import DB.StudentParser;
import enums.Bool;
import enums.Code1;
import enums.Code1.FileType;
import enums.Code1.Page;
import enums.Code2;
import enums.Direction;
import enums.Gender;
import enums.ProtocolType;
import models.Account;
import models.Application;
import models.Bill;
import models.Document;
import models.Dormitory;
import models.PlacementHistory;
import models.Schedule;
import models.ScheduleCode;
import models.Tuple;
import utils.Protocol;
import utils.ProtocolHelper;
import utils.SocketHelper;

//디버깅용 클래스
//대충 클라이언트에서 어떤 요청이 왔을때 그에 대한 반응(로직)을 모아둠.
//주석으로 로직을 설명하고, 실제 동작가능한 코드도 넣어라.
//실제 네트워킹이 정상적으로 작동하면, 이 클래스를 삭제하고 코드를 적절한 위치에 옮기도록.
//로직을 변경하게 될 경우 맨 아래 주석에 로그를 남겨주세요!!!

//학기코드
//201901 : 1학기
//201902 : 여름계절
//201903 : 2학기
//201904 : 겨울계절

public class Responser
{
	//아래 규칙을 지켜주세요.
	//(1) 메소드 위에 붙는 모든 주석은 클라이언트에서의 동작이다.
	//(2) 메소드 안에 있는 주석 및 코드는 서버에서의 동작이다.
	//(3) 괄호가 쳐진 인덱스와 내용은 클라이언트에서의 동작이다.
	//(4) 주석 아래에 그 동작에 해당하는 메소드를 적는다.
	//(5) 전체적인 흐름을 한눈에 파악할 수 있도록, 코드가 길어지면 다른클래스로 분할한다.
	//(6) IF문과 같은 간단한 제어문은 있어도 상관없다.
	
//	[예시]
//	학생 - 생활관 입사 신청 - 들어왔을 때
//	public void student_submitApplicationPage_onEnter()
//	{
//		//1. 스케쥴을 확인하고 입사 신청 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
//		boolean isPassed = DifferentClass.checkSchedule();
//		
//		//2. 받은 요청의 헤더에서 학번을 알아낸다.
//		Account a = (Account) ProtocolHelper.deserialization(protocol.getBody());		
//   	//String id = a.accountId;
//		
//		//3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
//		Gender g = DifferentClass2.getGender();
//		
//		//4. 생활관 테이블에서 이번 학기에 해당하고, 성별에 해당하는 기숙사 정보 목록을 가져온다.
//		ArrayList<Dormitory> dormList = DifferentClass3.getDorms();
//		
//		//5. 가져와야할 정보는 생활관 테이블의 생활관명, 기간구분(없으면말고), 식사구분, 5일식 식비, 7일식 식비, 관리비,
//		//	 스케쥴 테이블에서 비고(안내사항)를 가져온다.
//		ArrayList<DataModel> rs = DBManager.getDatas();
//		
//		//6. 해당 정보를 객체화, 배열로 만들어 클라이언트에게 전송한다.
//		Networking(rs);
//	}
	
	//----------------------------------유틸리티---------------------------------------------------
	
	//클라이언트 이벤트에 대한 회신
	private static void eventReply(SocketHelper socketHelper, Serializable data)
	{
		try
		{
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(data)).build());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return;
	}
	
	private static Tuple<Bool, String> createMessage(Bool isSucceed, String msg)
	{
		return new Tuple<Bool, String>(isSucceed, msg);
	}
	
	private static void sendDocumentType(SocketHelper socketHelper)
	{
		ArrayList<FileType> fileTypeList = new ArrayList<FileType>();
		fileTypeList.add(FileType.MEDICAL_REPORT);
		fileTypeList.add(FileType.OATH);
		
		eventReply(socketHelper, fileTypeList);
		return;
	}
	
	//------------------------------------------------------------------------------------------------
	
	//학생 - 생활관 입사 신청 - 들어왔을 때
	public static void student_submitApplicationPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 입사 신청 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		boolean isAdmissible = ScheduleParser.isAdmissible((Page)protocol.code1);
		System.out.println("스케쥴 체크됨");
		Tuple<String, ArrayList<Dormitory>> failMessage;
		if(!isAdmissible)
		{
			//스케쥴 때문에 진입 불가한 경우
			failMessage = new Tuple<String, ArrayList<Dormitory>>("현재 생활관 입사 신청 기간이 아닙니다.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		//2. 받은 요청의 헤더에서 학번을 알아낸다.
		Account account = (Account) ProtocolHelper.deserialization(protocol.getBody());
		System.out.println("전달받은 학번 : " + account.accountId);
		
		//3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
		Gender gender = StudentParser.getGender(account.accountId);
		if(gender == null)
		{
			System.out.println("성별이 조회되지 않음.");
			//학생이 조회되지 않은 경우
			failMessage = new Tuple<String, ArrayList<Dormitory>>("해당 학생이 조회되지 않았습니다. 관리자에게 문의하세요.", null);
			eventReply(socketHelper, failMessage);
			return ;
		}
		System.out.println("성별 불러옴 : " + gender.toString() );
		
		//4. 생활관 테이블에서 이번 학기에 해당하고, 성별에 해당하는 기숙사 정보 목록을 가져온다.
		//	 가져와야할 정보는 생활관 테이블의 생활관명, 기간구분(없으면말고), 식사구분, 5일식 식비, 7일식 식비, 관리비,
		int semester = CurrentSemesterParser.getCurrentSemester();
		if(semester == 0)
		{
			//현재 학기 조회 실패
			failMessage = new Tuple<String, ArrayList<Dormitory>>("현재 학기 조회 실패. 관리자에게 문의하세요.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		ArrayList<Dormitory> dormitoryList = DormParser.getDormitoryList(semester, gender);
		if(dormitoryList == null)
		{
			//기숙사 목록 조회 실패
			failMessage = new Tuple<String, ArrayList<Dormitory>>("기숙사 목록 조회 실패. 관리자에게 문의하세요.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		//5. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
		String description = ScheduleParser.getDescription(Code1.Page.입사신청);
		if(description == null)
		{
			//기숙사 목록 조회 실패
			failMessage = new Tuple<String, ArrayList<Dormitory>>("안내사항 조회 실패. 관리자에게 문의하세요.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		//6. 해당 정보를 객체화, 배열로 만들어 클라이언트에게 전송한다.
		Tuple<String, ArrayList<Dormitory>> resultTuple = new Tuple(description, dormitoryList);
		
		//전송한다.
		eventReply(socketHelper, resultTuple);
	}
	
	//학생 - 생활관 입사 신청 - 등록 버튼 클릭 시
	public static void student_submitApplicationPage_onSubmit(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 받은 요청의 헤더에서 학번을 알아낸다.
		Tuple<Account, ArrayList<Application>> t =  (Tuple<Account, ArrayList<Application>>) ProtocolHelper.deserialization(protocol.getBody());
		String id = t.obj1.accountId;
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역이 있는지 조회 -> TRUE 이면 내역 취소하고 하라고 클라이언트에게 알려줌. FALSE이면 다음으로
		
		boolean isPrevAppExist = true;
		try 
		{
			isPrevAppExist = ApplicationParser.isExist(id);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청 내역 조회 중 오류가 발생하였습니다."));
			return;
		}
		
		if(isPrevAppExist)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "이전 신청 정보를 삭제해 주세요."));
			return;
		}
		
		//3. 받은 데이터를 역직렬화한다. ([생활관구분, 기간구분, 식사구분] x4 와 휴대전화번호, 코골이여부가 나옴)
		@SuppressWarnings("unchecked")
		ArrayList<Application> A = t.obj2;
		//4. 해당 배열을 신청 데이트에 INSERT한다.
		Iterator<Application> appIter = A.iterator();
		Gender gender = StudentParser.getGender(t.obj1.accountId);
		if(gender == null)
		{
			//학생이 조회되지 않은 경우
			eventReply(socketHelper, createMessage(Bool.FALSE, "해당 학생이 조회되지 않았습니다. 관리자에게 문의하세요."));
			return;
		}
		int semester = CurrentSemesterParser.getCurrentSemester();
		if(semester == 0)
		{
			//현재 학기 조회 실패
			eventReply(socketHelper, createMessage(Bool.FALSE, "현재 학기 조회 실패. 관리자에게 문의하세요."));
			return;
		}
		while(appIter.hasNext())  //(int choice, String mealType, Bool isSnore, String dormitoryName, Gender gender, int semesterCode, String id)
		{
			Application temp = appIter.next();
			temp.setGender(gender.gender.charAt(0));
			temp.setSemesterCode(semester);
			ApplicationParser.insertApplication(temp.getChoice(), temp.getMealType(), temp.isSnore(), temp.getDormitoryName(), temp.getGender(), temp.getSemesterCode(), id); //이거 풀하고 다시 짤거에요
			//이것도 실패했을때 클라이언트한테 알려줘라.
		}
		//5. 클라이언트에게 성공 여부를 알려준다.(성공/DB연결 오류로 인한 실패/DB사망/알수없는오류 등등...)
		// 여기 있는 모든 배열 = ArrayList 진짜 []로 짜시면 안돼요 / 배열을 ArrayList로 대체함 진짜 대체만 했으니 나머지 알고리즘은 완성해주세요

		boolean isApplicationExist = false;
		try {
			isApplicationExist = ApplicationParser.isExist(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//DB에 들어갔는지 체크함.
		if(isApplicationExist)
		{
			eventReply(socketHelper, createMessage(Bool.TRUE, "신청에 성공했습니다."));
			return;
		}
		else
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청에 실패했습니다."));
			return;
		}
	}
	
	//학생 - 생활관 입사 신청 - 취소 버튼 클릭 시 (2019-12-08 명근 수정)
	public static void student_submitApplicationPage_onCancel(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException, SQLException
	{
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		Account account = (Account) ProtocolHelper.deserialization(protocol.getBody());	
		String id = account.accountId;
		
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역이 있는지 조회 -> TRUE 이면 다음으로, FALSE이면 클라이언트에게 내역 없다고 알려줌.
		boolean isExist = false;
		try {
			isExist = ApplicationParser.isExist(id);
		} catch (Exception e) {
			e.printStackTrace();
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청 내역 조회 중 오류가 발생했습니다."));
			return;
		}
		
		if(!isExist)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청내역이 존재하지 않습니다."));
			return;
		}
		
		
		try
		{
			ApplicationParser.deleteApplication(id);
			eventReply(socketHelper, createMessage(Bool.TRUE, "신청내역 삭제에 성공했습니다."));
			return;
		}
		catch(Exception e)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청내역 삭제에 실패했습니다."));
			return;
		}
		
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 생활관 신청 조회 - 들어왔을 때
	public static void student_CheckApplicationPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 신청내역 조회 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		boolean isAdmissible = false;
		try
		{
			isAdmissible = ScheduleParser.isAdmissible((Page)protocol.code1);
		}
		catch(Exception e)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 조회 중 오류가 발생했습니다."));
			return;
		}
		
		if(!isAdmissible)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청조회기간이 아닙니다."));
			return;
		}
		
		//2. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
		String notice = "";
		try
		{
			notice = ScheduleParser.getDescription((Page)protocol.code1);
		}
		catch(Exception e)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "안내사항 조회 중 오류가 발생했습니다."));
			return;
		}
		//3. 스케쥴 객체를 클라이언트에게 전송한다.
		eventReply(socketHelper, createMessage(Bool.TRUE, notice));
		//(4. 클라이언트에서는 받은 비고(안내사항)을 표시한다)
	}
	
	//학생 - 생활관 신청 조회 - 조회 버튼 클릭 시

	public static void student_CheckApplicationPage_onCheck(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException, SQLException
	{
		//명근 수정. 2019-12-09 15:54
		//그냥 통신 두번하는걸로 생각함.
		//클라이언트가 요청하면 서버가 신청목록 한번 보내고.
		//그 다음 서버가 바로 선발결과 한번 또 보내고.

		String studentId = "";
		try
		{
			Account account = (Account) ProtocolHelper.deserialization(protocol.getBody());		
			studentId = account.accountId;
		}
		catch(Exception e)
		{
			//계정 조회 중 오류 발생.
			System.out.println("계정 조회 중 오류 발생");
			Tuple<String, ArrayList<Application>> failMessage = new Tuple<String, ArrayList<Application>>("해당 계정이 존재하지 않습니다.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		//신청 목록 조회
		ArrayList<Application> applicationResult;
		try
		{
			applicationResult = ApplicationParser.getApplicationResult(studentId);
		}
		catch(Exception e)
		{
			//신청 목록 조회 중 오류 발생
			System.out.println("신청 목록 조회 중 오류 발생");
			Tuple<String, ArrayList<Application>> failMessage = new Tuple<String, ArrayList<Application>>("신청 목록 조회 중 오류가 발생하였습니다.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		//선발결과 조회. 합격여부 Y인것만 가져와라
		ArrayList<Application> passedApplicationResult = null;
		try
		{
			passedApplicationResult = ApplicationParser.getPassedApplication(studentId);
		}
		catch(Exception e)
		{
			//선발결과 조회 중 오류 발생
			System.out.println("선발결과 조회 중 오류 발생");
			Tuple<String, ArrayList<Application>> failMessage = new Tuple<String, ArrayList<Application>>("선발 결과 조회 중 오류가 발생하였습니다.", null);
			eventReply(socketHelper, failMessage);
			return;
		}
		
		//신청 목록, 선발 결과 전송
		System.out.println("신청 목록, 선발 결과 전송");
		Tuple<ArrayList<Application>, ArrayList<Application>> passList = new Tuple<ArrayList<Application>, ArrayList<Application>>(applicationResult, passedApplicationResult);
		eventReply(socketHelper, passList);
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 생활관 고지서 조회 - 들어왔을 때
	public static void student_CheckBillPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 고지서 조회 가능한 날짜인지 조회 -> TRUE이면 괜찮다고 클라이언트에게 전송, FALSE이면 못들어가게 막음
		boolean isAdmissible = false;
		try
		{
			//클라이언트에게서 받은 프로토콜의 페이지코드가 고지서조회로 설정되있음.
			//아래는 고지서 조회가 진입가능한지 묻는 코드임.
			isAdmissible = ScheduleParser.isAdmissible((Page)protocol.code1);
		}
		catch(Exception e)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "고지서 조회 중 오류가 발생하였습니다."));
			return;
		}
		
		if(!isAdmissible)
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "고지서 조회 기간이 아닙니다."));
			return;
		}
		
		eventReply(socketHelper, createMessage(Bool.TRUE, "고지서 조회 가능."));
	
	}
	
	//학생 - 생활관 고지서 조회 - 조회 버튼 클릭 시
	public static void student_CheckBillPage_onCheck(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException, SQLException
	{
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		Account account = (Account) ProtocolHelper.deserialization(protocol.getBody());		
		String studentId = account.accountId;
		
		boolean isExist = false;
		
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 합격여부가 T인 내역 조회 -> 내역 있으면 다음으로, 없으면 없다고 클라이언트에게 알려줌
		try
		{
			isExist = ApplicationParser.isExistPassState(studentId);
		}
		catch(Exception e)
		{
			System.out.println("합격 여부 조회 중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "합격 여부 조회 중 오류가 발생하였습니다."));
			return;
		}
		
		if(!isExist)
		{
			System.out.println("합격 내역이 존재하지 않음");
			eventReply(socketHelper, createMessage(Bool.FALSE, "합격 내역이 존재하지 않습니다."));
			return;
		}
		
		//3. 합격한 신청 내역에 관한 납부해야 할 비용을 저장
		int cost = -1;
		try
		{
			cost = DormParser.getCheckBillCost(studentId);			
		}
		catch(Exception e)
		{
			System.out.println("납부비용 조회 중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "납부비용 조회에 실패하였습니다."));
			return;
		}
		
		//4. 랜덤 생성한 계좌번호와, 은행 명, 계산한 비용을 객체화해서 클라이언트에게 전송한다.
		Bill bill = createRandBill(cost);
		Tuple<Bool, Bill> sendData = new Tuple<Bool, Bill>(Bool.TRUE, bill);
		eventReply(socketHelper, sendData);
		//(. 클라이언트는 이걸 받아서 대충 메모장으로 띄워준다.)
	}
	
	//이런건 좀 함수로 분리하세요... (명근, 2019-12-09 19:23 수정)
	private static Bill createRandBill(int cost)
	{
		Random rand = new Random();
		int accountNum = rand.nextInt(100)+1000;
		Bill bill = new Bill("농협", accountNum, cost);
		return bill;
	}
	//------------------------------------------------------------------------
	
	//학생 - 생활관 호실 조회 - 들어왔을 때
	public static void student_checkRoomPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 호실 조회 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		boolean isAdmissible = false;
		try
		{
			isAdmissible = ScheduleParser.isAdmissible((Page)protocol.code1);
		}
		catch(Exception e)
		{
			System.out.println("스케쥴 조회 중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 조회 중 오류가 발생했습니다."));
			return;
		}
		
		if(!isAdmissible)
		{
			System.out.println("호실 조회 기간 아님.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "호실 조회 기간이 아닙니다."));
			return;
		}
		
		//2. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
		String notice = "";
		try
		{
			notice = ScheduleParser.getDescription((Page)protocol.code1);
		}
		catch(Exception e)
		{
			System.out.println("안내사항 조회 중 오류가 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "안내사항 조회 중 오류가 발생하였습니다."));
			return;
		}
		//3. 스케쥴 객체를 클라이언트에게 전송한다.
		eventReply(socketHelper, createMessage(Bool.TRUE, notice));
		//(4. 클라이언트에서는 받은 비고(안내사항)을 표시한다)
	}
	
	//학생 - 생활관 호실 조회 - 조회 버튼 클릭 시
	public static void student_checkRoomPage_onCheck(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException, SQLException
	{
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		Account account = (Account) ProtocolHelper.deserialization(protocol.getBody());		
		String studentId = account.accountId;
		
		//3-2. 내역이 있는 경우 신청 테이블에서 최종합격여부, 납부여부, 식비구분, 생활관, 호실유형(이건 일반실 고정)을 조회한다.
		Application lastPassedApplication;
		try
		{
			lastPassedApplication = ApplicationParser.getLastPassedApplication(studentId);
		}
		catch(Exception e)
		{
			System.out.println("신청내역 조회 중 오류가 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청내역 조회 중 오류가 발생하였습니다."));
			return;
		}
		
		if(lastPassedApplication == null)
		{
			System.out.println("신청내역이 존재하지 않습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청내역이 존재하지 않습니다."));
			return;
		}
		
		//4. 배정내역 테이블에서 해당 학번이 배정되있는 호실과 침대번호를 가져온다.
		PlacementHistory history;
		try
		{
			history = PlacementHistoryParser.getPlacementResult(studentId);
		}
		catch(Exception e)
		{
			System.out.println("배정내역 조회 중 오류가 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "배정내역 조회 중 오류가 발생하였습니다."));
			return;
		}
		
		if(history == null)
		{
			System.out.println("배정내역이 존재하지 않습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "배정내역이 존재하지 않습니다."));
			return;
		}
		
		//어플리케이션과 히스토리를 묶어서 보내주자
		Tuple<Application, PlacementHistory> resultTuple = new Tuple(lastPassedApplication, history);
		
		//5. 3-2와 4를 합쳐 객체화한다. 그리고 이것을 클라이언트에게 전송한다.
		eventReply(socketHelper, resultTuple);
		//(6. 클라이언트는 받은 객체를 역직렬화, UI에 표기한다)
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 서류 제출 - 들어왔을 때
	public static void student_submitDocumentPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//실제 원스톱을 기반으로, 학생이 서류 제출하는건 아무때나 할 수 있다고 하였다.
		//1. 서류 유형을 객체화 배열화하여 클라이언트로 전송한다.
		sendDocumentType(socketHelper);
		return;
		//(2. 클라이언트는 받은 배열을 역직렬화하여 서류유형 combobox에 표시한다)
	}
	
	//학생 - 서류 제출 - 제출 버튼 클릭 시(파일 업로드)
	public static void student_submitDocumentPage_onSubmit(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 서버 컴퓨터 내 저장할 공간에 빈공간이 10MB보다 큰지 확인한다. -> 빈공간이 10MB보다 크면 진행, 작으면 클라이언트에게 안된다고 알려줌.
		//2. 헤더에서 파일 유형이 결핵진단서인지, 서약서인지 확인한다.
		//3. 현재 날짜로부터 학기를 특정한다. (학기는 201901 ~ 201906)
		//4. 어느폴더\학번\학기\학번+결핵진단서or서약서.jpg 와 같은 형식으로 저장된다.
		//	 (학기가 겹치면 덮어씌워진다. 즉, 한학기에 한 파일만 유효함)
		//5. 파일 저장 성공/실패 여부를 클라이언트에게 알려준다.
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 서류 조회 - 들어왔을 때
	public static void student_checkDocumentPage_onEnter(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 서류 유형을 객체화 배열화하여 클라이언트로 전송한다.
		sendDocumentType(socketHelper);
		return;
		//(2. 클라이언트는 받은 배열을 역직렬화하여 서류유형 combobox에 표시한다)
	}
	
	//학생 - 서류 조회 - 조회 버튼 클릭 시
	public static void student_checkDocumentPage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 받은 요청의 헤더에서 학번, 서류유형을 알아낸다. 
		String studentId;
		Code1.FileType fileType;
		Document document = null;
		try 
		{
			@SuppressWarnings("unchecked")
			Tuple<Account, Code1.FileType> temp = (Tuple<Account, Code1.FileType>) ProtocolHelper.deserialization(protocol.getBody());
			studentId = temp.obj1.accountId;
			fileType = temp.obj2;
		}
		catch (Exception e) 
		{
			System.out.println("클라이언트가 송신한 계정과 파일유형이 이상하다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "클라이언트가 송신한 튜플이 이상합니다. 관리자에게 문의하세요. Code-404"));
			return;
		}
		
		try
		{
			document = DocumentParser.findDocument(studentId, fileType);
		}
		catch(Exception e)
		{
			System.out.println("서류를 조회하는데 실패.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서류를 조회하는데 실패하였습니다."));
			return;
		}
		
		if(document == null)
		{
			System.out.println("서류 제출 내역 없음, 혹은 DocumentParser.findDocument 안에서 예외터짐");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서류 제출 내역이 없습니다."));
			return;
		}
		//2. 서류 테이블에서 해당 학번이 이번 학기에 제출한 내역 중 서류유형이 일치하는 것을 찾는다. -> 있으면 진행, 없으면 없다고 알려줌
		//3. 서류 테이블에서 서류유형, 제출일시, 진단일시, 파일경로를 알아내어 객체화한다.
		//4. 클라이언트에게 전송한다.
		eventReply(socketHelper, new Tuple<Bool, Document>(Bool.TRUE, document));
	}
	
	//학생 - 서류 조회 - 다운로드 버튼 클릭 시(파일 다운로드)
	public static void student_checkDocumentPage_onDownlaod(Protocol protocol, SocketHelper socketHelper)
	{
		//(1. 클라이언트는 자기 남은 용량이 10MB 이상인지 체크한다)
		//(2. 클라이언트는 해당 파일 경로를 바디에 담아 서버에게 보낸다)
		//3. 서버는 해당 파일 경로로 파일을 찾는다. -> 파일이 있으면 진행, 없으면 없다고 알려줌(이땐 버그라고 봐야할듯)
		//4. 파일을 찾았으면 클라이언트에 업로드한다.
	}
	
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	
	//관리자 - 선발 일정 조회 및 관리 - 들어왔을 때
	public static void admin_scheduleManagePage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴 할일 코드 테이블에서 '코드', '이름' 을 객체로 만들어 배열로 가져온다.
		ArrayList<ScheduleCode> scheduleList = null;
		try
		{
			scheduleList = ScheduleParser.getScheduleCode();			
		}
		catch(Exception e)
		{
			System.out.println("스케쥴 할일 코드 조회 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 할일 코드 조회에 실패하였습니다."));
			return;
		}
		
		if(scheduleList == null)
		{
			System.out.println("스케쥴 할일 코드 목록 비어있음.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 할일 코드 목록이 비어있습니다."));
			return;
		}
		
		//2. 객체 배열을 직렬화하여 클라이언트로 전송한다.
		eventReply(socketHelper, new Tuple<Bool, ArrayList<ScheduleCode>>(Bool.TRUE, scheduleList));
		
		//(3. 클라이언트는 등록 gridView 안의 유형 combobox에 값을 채워준다.)
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_scheduleManagePage_onCheck(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴 할일 코드 테이블에서 목록을 객체로 만들어 배열로 가져온다. (ID, 할일코드, 시작일, 종료일, 비고)
		ArrayList<Schedule> scheduleList = null;
		try
		{
			scheduleList = ScheduleParser.getAllSchedule();
		}
		catch(Exception e)
		{
			System.out.println("스케쥴 조회 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 조회에 실패하였습니다."));
			return;
		}
		
		if(scheduleList == null)
		{
			System.out.println("스케쥴 조회 목록이 비어있음");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 조회 목록이 비어있습니다."));
			return;
		}
		
		//3. 스케쥴 객체 배열을 클라이언트로 전송한다.
		eventReply(socketHelper, new Tuple<Bool, ArrayList<Schedule>>(Bool.TRUE, scheduleList));
		
		//(4. 클라이언트는 받아서 tableView에 표시한다. 클라이언트에는 ID, 할일이름, 시작일, 종료일, 비고가 표시된다)
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 삭제 버튼 클릭 시
	public static void admin_scheduleManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트에게서 스케쥴 ID를 받는다.
		//2. 스케쥴 테이블에서 해당 ID로 탐색한다.
		//3-1. DB에 해당 ID가 존재한다. -> DB에 DELETE 요청
		//3-2. DB에 해당 ID가 존재하지 않는다. -> 없다고 클라이언트에게 알려줌.
		//4. DELETE 요청 결과를 클라이언트에게 알려준다. (성공/실패/아마존 사망...etc)
		
		String scheduleId = null;
		try
		{
			scheduleId = (String) ProtocolHelper.deserialization(protocol.getBody());
		}
		catch(Exception e)
		{
			System.out.println("클라이언트에서 받아온 스케쥴이 이상합니다. 관리자에게 문의해주세요. code-406");
			eventReply(socketHelper, createMessage(Bool.FALSE, "클라이언트에서 받아온 스케쥴이 이상함."));
			return;
		}
		
		if(scheduleId == null)
		{
			System.out.println("서버에서 빈 아이디 수신.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서버에서 빈 아이디를 수신했습니다."));
			return;
		}
		
		boolean isExist = false;
		
		try
		{
			isExist = ScheduleParser.isExist(scheduleId);
		}
		catch(Exception e)
		{
			System.out.println("스케쥴 ID로 조회 중 예외 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 ID로 조회 중 예외 발생했습니다."));
			return;
		}
		
		if(!isExist)
		{
			System.out.println("조회된 스케쥴이 없음.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "조회된 스케쥴이 없습니다."));
			return;
		}
		
		//스케쥴 DELETE 메소드 호출해라.
		try
		{
			ScheduleParser.deleteSchedule(scheduleId);
		}
		catch(Exception e)
		{
			System.out.println("스케쥴 삭제 쿼리 실패.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 삭제에 실패하였습니다."));
			return;
		}
		
		eventReply(socketHelper, createMessage(Bool.TRUE, "스케쥴 삭제에 성공하였습니다."));
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 등록 버튼 클릭 시
	public static void admin_scheduleManagePage_onInsert(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트에게서 유형코드, 시작일, 종료일, 비고를 받는다. (유형은 코드로 전달되어야 한다. String으로 전달받는건 미개함
		//	 그래서 유형 이름을 받는게 아니라 유형 코드를 받는것)
		//2. 스케쥴 테이블에서 유형, 시작일, 종료일이 중복되는지 체크한다.
		//3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다 
		//3-2. 기존 값이 존재하지 않으면 INSERT한다.
		//4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc)
		
		Schedule schedule = null;
		try
		{
			schedule = (Schedule) ProtocolHelper.deserialization(protocol.getBody());
		}
		catch(Exception e)
		{
			System.out.println("클라이언트에서 받아온 스케쥴이 이상함.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "클라이언트에서 받아온 스케쥴이 이상합니다. 관리자에게 문의하세요. code-407"));
			return;
		}
		
		if(schedule == null)
		{
			System.out.println("클라이언트에서 받아온 스케쥴이 없음.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "클라이언트에서 받아온 스케쥴이 없습니다."));
			return;
		}
		
		//시간 순서가 엇갈리는가?
		if(schedule.startDate.after(schedule.endDate))
		{
			System.out.println("시간 순서가 엇갈림.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "시간 순서가 엇갈립니다."));
			return;
		}
		
		//중복체크
		boolean isExist = false;
		try
		{
			isExist = ScheduleParser.isExist(schedule);
		}
		catch(Exception e)
		{
			System.out.println("중복체크 하던 도중 에러 발생.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 중복 체크 도중 에러가 발생하였습니다. 관리자에게 문의하세요. code-408"));
			return;
		}
		
		if(isExist)
		{
			System.out.println("중복값 존재");
			eventReply(socketHelper, createMessage(Bool.FALSE, "코드, 시작일, 종료일이 모두 일치한 중복값이 존재합니다. 삭제하고 등록해주세요."));
			return;
		}
		boolean isSuceed = false;
		try
		{
			isSuceed = ScheduleParser.insertSchedule(schedule);
		}
		catch(Exception e)
		{
			System.out.println("스케쥴 등록 도중 오류가 발생.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 등록 도중 오류가 발생하였습니다."));
			return;
		}
		
		if(!isSuceed) 
		{
			System.out.println("스케쥴 등록에 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "스케쥴 등록에 실패하였습니다."));
			return;
		}
		
		//성공!
		eventReply(socketHelper, createMessage(Bool.TRUE, "스케쥴 등록에 성공하였습니다."));
	}
	
	//-------------------------------------------------------------------------

	public static void admin_dormitoryManagePage_onEnter(Protocol protocol, SocketHelper socketHelper) {
	}

	//관리자 - 생활관 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_dormitoryManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 생활관 정보 테이블에서 모든 정보를 가져와 객체화한다. (생활관명, 학기, 수용인원, 식사의무, 5일식 식비, 7일식 식비, 기숙사비
		ArrayList<Dormitory> dorm;
		try
		{
			dorm = DormParser.getAllDormitories();
		}
		catch (SQLException e)
		{
			System.out.println("생활관 목록 불러오기 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "생활관 목록을 불러오지 못했습니다."));
			return;
		}
		
		eventReply(socketHelper, new Tuple<Bool, ArrayList<Dormitory>>(Bool.TRUE, dorm));
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 생활관 조회 및 관리 - 삭제 버튼 클릭 시
	public static void admin_dormitoryManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 생활관명, 학기로 생활관 정보 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
		
		String dormName = null;
		String semester = null;
		
		try
		{
			Tuple<String, String> receivedTuple = (Tuple<String, String>) ProtocolHelper.deserialization(protocol.getBody());
			dormName = receivedTuple.obj1;
			semester = receivedTuple.obj2;
		}
		catch (Exception e)
		{
			System.out.println("클라이언트에서 받아온 생활관명 학기 읽기 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서버가 생활관명, 학기를 읽는데 실패하였습니다."));
			return;
		}
		
		boolean isExist = false;
		try
		{			
			isExist = DormParser.isExist(dormName, semester);
		}
		catch(Exception e)
		{
			System.out.println("생활관 존재여부 확인 중 에러 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "생활관 존재여부 확인 중 에러가 발생하였습니다."));
			return;
		}
		if(!isExist)
		{
			System.out.println("해당되는 생활관을 찾지 못하였습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "해당되는 생활관을 찾지 못하였습니다."));
			return;
		}
		
		boolean isSucceed = false;
		try
		{			
			isSucceed = DormParser.deleteDormitory(dormName, semester);	
		}
		catch(Exception e)
		{		
			System.out.println("생활관 삭제 도중 에러 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "생활관 삭제 중 에러가 발생하였습니다."));
			return;
		}
		if(!isSucceed)
		{
			System.out.println("생활관 삭제에 실패하였습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "생활관 삭제에 실패하였습니다."));
			return;
		}
		
		eventReply(socketHelper, createMessage(Bool.TRUE, "생활관 삭제에 성공하였습니다."));
	}
	
	//관리자 - 생활관 조회 및 관리 - 등록 버튼 클릭 시
	public static void admin_dormitoryManagePage_onInsert(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트에게서 생활관명, 학기, 수용인원, 식사의무, 5일식 식비, 7일식 식비, 기숙사비를 받는다.
		//2. 생활관 정보 테이블에서 생활관명, 학기로 탐색, 중복되는 값이 있는지 체크한다.
		//3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다.
		//3-2. 기존 값이 존재하지 않으면 INSERT한다.
		//4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc)
		
		Dormitory dormitory = null;
		
		try
		{
			dormitory = (Dormitory) ProtocolHelper.deserialization(protocol.getBody());
		}
		catch (Exception e)
		{
			System.out.println("클라이언트에서 받아온 생활관 읽기 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서버가 생활관을 읽는데 실패하였습니다."));
			return;
		}
		
		boolean isSucceed = false;
		try
		{
			//누가좀 해줘. 기숙사 삭제하는 SQL임
//			isSucceed = DormParser.insertDormitory(dormitory);	
		}
		catch (Exception e)
		{
			System.out.println("생활관 등록 도중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "생활관 등록 도중 오류가 발생하였습니다."));
			return;
		}
		
		if(!isSucceed)
		{
			System.out.println("생활관 등록에 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "생활관 등록에 실패하였습니다."));
			return;
		}
		
		eventReply(socketHelper, createMessage(Bool.TRUE, "생활관 등록에 성공하였습니다."));
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 입사 선발자 조회 및 관리 - 입사자 선발 버튼 클릭 시
	public static void admin_selecteesManagePage_onSelection(Protocol protocol, SocketHelper socketHelper)
	{
		//입사자 선발 버튼은 신청 목록에서 합격여부를 Y로 바꾸는 역할을 한다
		//ex) 100명을 뽑아야되면 성적순으로 정렬 뒤 총 신청자 중에서 상위 100명까지 합격여부를 Y로 바꾼다?
		//이건 선발 짜놓은 친구들이 구현해놨으니 거기에 맞게 로직 고치면 됨.(로직 주석 구체적으로 달아주셈, 어떻게 돌아가는지 다른애들도 알수있게)
		
		//1. 클라이언트에게 입사자 선발 요청을 받는다. (바디에는 딱히 아무것도 없다. 요청을 위한 통신)
		//2. 선발 알고리즘을 시행한다. (대충 생각해본것임. 더 나은 알고리즘, 이미 구현한 알고리즘 사용해도 됨. 그 경우 아래 알고리즘을 고쳐주셈)
		boolean isSucceed = false;
		try 
		{
			logic.ResidentSelecter.selectionByChoice();
			isSucceed = true;
		} 
		catch (Exception e) 
		{
			System.out.println("입사자 선발 도중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사자 선발 도중 오류가 발생하였습니다."));
			return;
		}
		
		//이것만 부르면 1, 2, 3, 4 전부 처리함
				//		1) 신청 내역에서 생활관별로 묶는다. - 정확히는 생활관별, 지망별로 묶어서 생활관별 0지망 -> 생활관별 1지망 -> 생활관별 2지망 -> 생활관별 3지망 순으로 뽑음
				//		2) 학생 한명당 평균성적을 계산한다.
				//		3) 평균성적순으로 정렬한다. - 정렬하는동안 그 학번으로 이미 합격한 신청이 있는지 확인하고 이미 있으면 정렬 대상에서 제외함
				//		4) 남은 자리가 n이면 n명까지 합격여부를 Y로 UPDATE - 맞워요
				//3. 결과를 클라이언트에게 알려준다(성공/실패?) - 이건 어케하노
		
		if(isSucceed)
		{
			eventReply(socketHelper, createMessage(Bool.TRUE, "입사자 선발에 성공했습니다."));
		}
		else
		{
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사자 선발에 실패했습니다."));			
		}
		
		//(TODO 명근, 2019-12-10 01:55, 이거 내가 대충 동작할거같이 만들었는데, 테스트는 무서워서 못해봄)
		
		return;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_selecteesManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 신청 테이블에서 이번 학기 신청 목록을 가져와 객체화한다. (학번, 생활관명, 학기, 지망, 몇일식, 납부여부, 합격여부, 최종결과, 코골이여부)
		//   (합격여부 Y, N인거 관계없이 가져와야될듯. 그래야 사실상 여기서 관리자가 신청내역 조회가능함)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		ArrayList<Application> appList = null;
		try 
		{
			appList = ApplicationParser.getAllApplications();
		} 
		catch (Exception e) 
		{
			System.out.println("입사 선발자 조회 도중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사 선발자 조회 도중 오류가 발생하였습니다."));
			return;
		}
		
		if(appList == null)
		{
			System.out.println("입사 선발자 목록이 조회되지 않음");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사 선발자 목록이 조회되지 않았습니다."));
			return;
		}

		Tuple<Bool, ArrayList<Application>> resultTuple = new Tuple<Bool, ArrayList<Application>>(Bool.TRUE, appList);
		eventReply(socketHelper, resultTuple);
		
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 삭제 버튼 클릭 시
	public static void admin_selecteesManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 생활관명, 학기, 지망으로 신청 테이블에서 조회한다.
		Application receivedApp;
		try 
		{
			receivedApp = (Application) ProtocolHelper.deserialization(protocol.getBody());
		} 
		catch (Exception e) 
		{
			System.out.println("클라이언트에서 받아온 삭제 신청 읽기 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서버가 삭제 신청을 읽는데 실패하였습니다."));
			return;
		}
		
		//DB에 삭제요청
		try
		{
			DB.ApplicationParser.deleteApplication(receivedApp);
		}
		catch(Exception e)
		{
			System.out.println("신청 삭제 도중 오류가 발생.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청 삭제 도중 오류가 발생하였습니다."));
			return;
		}
		

		eventReply(socketHelper, createMessage(Bool.TRUE, "신청 삭제에 성공하였습니다."));
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 입사자 조회 및 관리 - 입사자 등록(배정) 버튼 클릭 시
	public static void admin_boarderManagePage_onAllocate(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, SQLException
	{
		//입사자 등록(배정) 버튼은 신청 목록에서 합격여부를 Y, 납부내역 Y, 결핵진단서 Y인 신청의 최종합격여부를 Y로 바꾼다.
	
		//이거 AssignAlgorithm.passUpdate(); 하시면 위 내용대로 동작합니다.
		//그리고나서 배정내역에 최종합격여부가 Y인 학생들을 배정한다.
		
		//AssignAlgorithm.batchStart();
		//이것도 로직 짜놓은 친구들이 구현해놨으니 거기에 맞게 로직 고치면 됨.(로직 주석 구체적으로 달아주셈, 어떻게 돌아가는지 다른애들도 알수있게)
		
		//1. 클라이언트에게 입사자 등록(배정) 요청을 받는다. (바디에는 딱히 아무것도 없다. 요청을 위한 통신)
		//2. 등록(배정) 알고리즘을 시행한다.
		// 1.디비에서 생활관 정보를 가져와서 생활관별로 수용인원만큼 자리를 만듬 호실. 자리 ABCD고려해서
		// 2. 그 자리에 이미 살고있는 학생들의 ID를 넣음
		// 3. 없는 자리에 생활관 신청 합격자들의 ID, 퇴사일을 넣음
		// 4.그 정보에 맞게 DB에 업데이트
		
		//이걸 batchStart로 묶어놨으니 그냥 이것만 실행하면 됨
		
		//(명근, 2019-12-10 14:57, 그래서 passUpdate야 아니면 batchStart야? 일단 batchStart가 주석해제되있어서 batchStart로 해둠
		try
		{
			//입사다 등록(배정) 실행
			AssignAlgorithm.batchStart();
		}
		catch(Exception e)
		{
			System.out.println("입사자 등록(배정) 도중 오류가 발생.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사자 등록(배정) 도중 오류가 발생하였습니다."));
			return;
		}
		
		eventReply(socketHelper, createMessage(Bool.TRUE, "입사자 등록(배정)에 성공하였습니다."));
	}
	
	//관리자 - 입사자 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_boarderManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 배정내역 테이블에서 이번 학기 배정내역 목록을 가져와 객체화한다. (학번, 호, 학기, 생활관명, 자리, 퇴사예정일)
		//2. 배열화한다.
		ArrayList<PlacementHistory> history = null;
		try 
		{
			history = PlacementHistoryParser.getAllResidence();
		} 
		catch (Exception e) 
		{
			System.out.println("배정내역 조회에 실패.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "배정내역 조회에 실패했습니다."));
			return;
		}
		
		if(history.isEmpty())
		{
			System.out.println("배정내역이 비어있음.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "배정내역이 비어있습니다."));
			return;
		}
		
		//3. 직렬화해서 클라이언트에 전송한다.
		
		eventReply(socketHelper, new Tuple<Bool, ArrayList<PlacementHistory>>(Bool.TRUE, history));
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 입사자 조회 및 관리 - 삭제 버튼 클릭 시
	public static void admin_boarderManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 호, 학기, 생활관명으로 배정내역 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//	   (신청 테이블에서 최종합격여부를 N으로 UPDATE해야할지는 모르겠음...)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
		
		PlacementHistory history = null;
		try 
		{
			history = (PlacementHistory) ProtocolHelper.deserialization(protocol.getBody());
		}
		catch (Exception e) 
		{
			System.out.println("배정내역 역직렬화 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서버에서 배정내역을 읽는데 실패했습니다."));
			return;
		}
		
		boolean isExist = false;
		try 
		{
			isExist = PlacementHistoryParser.isExistPlcementHistory(history.studentId);
			
		} 
		catch (Exception e) 
		{
			System.out.println("배정내역 존재여부 확인 중 오류 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "배정내역 존재여부 확인 중 오류가 발생했습니다."));
			return;
		}
		
		if(!isExist)
		{
			System.out.println("해당 배정내역이 존재하지 않음.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "해당 배정내역이 존재하지 않습니다."));
			return;
		}
		
		try
		{
			PlacementHistoryParser.deletePlacamentHistory(history.studentId);
		}
		catch (Exception e) 
		{
			System.out.println("입사자 삭제 도중 오류 발생.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사자 삭제 도중 오류가 발생했습니다."));
			return;
		}
		
		eventReply(socketHelper, createMessage(Bool.TRUE, "입사자 삭제에 성공했습니다."));
		
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 등록 버튼 클릭 시
	@SuppressWarnings("unchecked")
	public static void admin_boarderManagePage_onInsert(Protocol protocol, SocketHelper socketHelper)
	{
		//배정내역에 학생을 임의로 추가하기 위한 기능
		//배정내역에 학생을 넣고, 신청 테이블에도 몇일식인지, 코골이여부를 기록하기 위해 INSERT해야됨.
		Tuple<PlacementHistory, Application> tuple = null;
		try 
		{
			tuple = (Tuple<PlacementHistory, Application>) ProtocolHelper.deserialization(protocol.getBody());
		} 
		catch (Exception e) 
		{
			System.out.println("클라이언트에서 받은 튜플 분석 실패.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서버가 요청을 분석하는데 실패했습니다."));
			return;
		}
		
		PlacementHistory history = tuple.obj1;
		Application application = tuple.obj2;
		
		boolean isExistPh = false;
		try 
		{
			isExistPh = PlacementHistoryParser.isExistPlcementHistory(history.studentId);
		}
		catch (Exception e) 
		{
			System.out.println("배정내역 조회 도중 오류가 발생");
			eventReply(socketHelper, createMessage(Bool.FALSE, "배정내역 조회 도중 오류가 발생하였습니다."));
			return;
		}
		
		if(isExistPh)
		{
			System.out.println("이미 같은 학번의 입사자가 존재.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "이미 같은 학번의 입사자가 존재합니다."));
			return;
		}
		
		Gender gender = null;
		try 
		{
			gender = StudentParser.getGender(history.studentId); 
		}
		catch (Exception e1)
		{
			System.out.println("성별 알아내는데 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "해당 학번의 성별을 조회하는데 실패했습니다."));
			return;
		}
		
		//getGender가 null로 반환됬을때 예외임... 혹시몰라 넣음.
		if(gender == null)
		{
			System.out.println("성별 알아내는데 실패. getGender가 null로 반환됨");
			eventReply(socketHelper, createMessage(Bool.FALSE, "해당 학번의 성별을 조회하는데 실패했습니다."));
			return;
		}
		
		try
		{
			char charGender = gender.gender.charAt(0);
			ApplicationParser.insertApplication(4, application.getMealType(), application.isSnore(), history.dormitoryName, 
					charGender, history.semester, history.studentId);
		}
		catch (Exception e)
		{
			System.out.println("입사자 등록 도중 오류 발생(신청테이블)");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사자 등록 도중 오류가 발생했습니다.(신청테이블)"));
			return;
		}
		
		try
		{
			PlacementHistoryParser.insertPlacementHistory(history);
		}
		catch (SQLException e)
		{
			System.out.println("입사자 등록 도중 오류 발생(배정내역 테이블)");
			eventReply(socketHelper, createMessage(Bool.FALSE, "입사자 등록 도중 오류가 발생했습니다.(배정내역 테이블)"));
			return;
		}
		
		eventReply(socketHelper, new Tuple<Bool, String>(Bool.TRUE, "입사자 등록에 성공했습니다."));
		//1. 클라이언트에게서 학번, 호, 학기, 생활관명, 자리, 퇴사예정일, 몇일식, 코골이여부를 받는다.
		//2. 배정내역 테이블에서 학번, 호, 학기, 생활관명으로 중복되는 값이 있는지 체크한다.
		//3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다.
		//3-2. 기존 값이 존재하지 않으면 INSERT한다.
		//	   신청 테이블에도 몇일식, 코골이여부 넣어주기위해 INSERT해줘야 한다.
		//4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc)
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 납부 여부 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_paymentManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 신청 테이블에서 이번 학기 신청 목록을 가져와 객체화한다. (학번, 생활관명, 학기, 지망, 몇일식, 납부여부, 합격여부, 최종결과, 코골이여부)
		//   (합격여부 Y 인 학생만 가져온다)
		//2. 배열화한다.
		ArrayList<Application> apps = new ArrayList<Application>();

		try
		{
			apps = ApplicationParser.getPassedApplication();
		}
		catch(Exception e)
		{
			System.out.println("납부여부 조회에 실패했습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "납부여부 조회에 실패했습니다."));
			return;
		}
		
		if(apps.isEmpty())
		{
			System.out.println("신청 테이블이 비어있습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "신청 테이블이 비어있습니다."));
			return;
		}
		
		eventReply(socketHelper, apps);
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 납부 여부 조회 및 관리 - 업데이트 버튼 클릭 시
		public static void admin_paymentManagePage_onUpdate(Protocol protocol, SocketHelper socketHelper) throws SQLException
		{
			//1. 클라이언트로부터 받은 학번, 생활관명, 학기로 납부여부 테이블에서 조회한다.
			
			//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
			Application ap = null;
			try 
			{
				ap = (Application) ProtocolHelper.deserialization(protocol.getBody());				
			}
			catch (ClassNotFoundException | IOException e) 
			{
				System.out.println("역직렬화 실패");
				eventReply(socketHelper, createMessage(Bool.FALSE, "해당되는 데이터가 없습니다."));
				return;
			}
			//2-1. 해당되는 데이터가 있으면 DB에 UPDATE쿼리를 쏜다.
			//(납부여부를 클라이언트에게서 받은 T/F로 UPDATE한다)	
			
			try {				
				ApplicationParser.updatePayCheck(ap);
				eventReply(socketHelper, createMessage(Bool.TRUE, "납부 여부 업데이트 성공"));
			} 
			catch(Exception e) {
				System.out.println("납부 여부 갱신 실패");
				eventReply(socketHelper, createMessage(Bool.FALSE, "납부 여부 갱신에 실패했습니다."));
				return;
			}
			
		}
	
	//관리자 - 납부 여부 조회 및 관리 - CSV 업로드 버튼 클릭 시
	public static void admin_paymentManagePage_onUpload(Protocol protocol, SocketHelper socketHelper)
	{
		//클라이언트로부터 CSV파일을 다운로드 받고, 이 CSV 파일로 신청 테이블에 납부여부를 Y로 바꾸기 위함.
		
		//(1. 클라이언트가 파일을 업로드 시도한다.)
		//2. 서버는 자신의 남은 용량이 10MB 이상인지 체크한다 -> 10MB 이상이면 진행, 10MB 이하이면 못받는다고 클라이언트에게 알려줌
		//3. 서버는 어느 위치에 CSV 파일을 저장한다.
		//4. 저장한 CSV 파일을 열어 학번 목록을 읽는다.
		//5. 신청 테이블에서 이번 학기에, CSV파일 내 학번이 존재하면 납부내역을 T로 UPDATE한다.
		//6. 만약 CSV파일 내에 학번이 존재하는데, 신청 테이블에 없는 경우 로그로 남기거나, 클라이언트에게 알려주면 좋겠다.
		//7. 결과를 클라이언트에게 알려준다. (학번 + 성공여부 String으로 보내줘도될듯, ex) 20191234 성공, 20191235 성공, 20191236 실패)
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 서류 조회 및 제출 - 들어왔을 때
	public static void admin_documentManagePage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 서류유형 ENUM을 배열화해서 목록을 만든다.
		//2. 배열화한 목록을 직렬화해서 클라이언트로 전송한다.
		//(3. 클라이언트는 받은 ENUM 배열을 역직렬화하여 서류유형 combobox에 표시한다)
		//[ENUM 배열화 예시]
		//arrayList<DocumentType> data = new arrayList<DocumentType>(DocumentType.MEDICAL, DocumentType.OATH);
		
		sendDocumentType(socketHelper);
		return;
	}
	
	//관리자 - 서류 조회 및 제출 - 조회 버튼 클릭 시
	public static void admin_documentManagePage_onCheck(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, SQLException
	{
		//1. 서류 테이블에서 이번 학기 서류제출내역 목록을 가져와 객체화한다. (학번, 서류유형, 제출일, 진단일, 서류저장경로, 유효여부)
		//2. 배열화한다.
		int semester = CurrentSemesterParser.getCurrentSemester();
		ArrayList<Document> dList = null;
		try
		{
			 dList = DocumentParser.getAllDocuments(semester);
		}
		catch(Exception e)
		{
			System.out.println("서류 조회에 실패했습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "납부여부 조회에 실패했습니다."));
			return;
		}
		
		if(dList.isEmpty())
		{
			System.out.println("서류 테이블이 비어있습니다.");
			eventReply(socketHelper, createMessage(Bool.FALSE, "서류 테이블이 비어있습니다."));
			return;
		}
		//3. 직렬화해서 클라이언트에 전송한다.
		eventReply(socketHelper, new Tuple<Bool, ArrayList<Document>>(Bool.TRUE, dList));		
		
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 서류 조회 및 제출 - 삭제 버튼 클릭 시
	public static void admin_documentManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 서류유형, 제출일로 서류 테이블에서 조회한다.
		Document docu = null;
		try 
		{
			docu = (Document) ProtocolHelper.deserialization(protocol.getBody());
			String id = docu.studentId;
			Code1.FileType filetype = docu.documentType;
			Date date = docu.submissionDate;
			//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
			try 
			{
				DocumentParser.deleteDocument(id, filetype, date);
				//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
				eventReply(socketHelper, createMessage(Bool.TRUE, "서류 삭제 성공"));
			} 
			catch (SQLException e)
			{
				System.out.println("서류 조회 및 제출 - delete문 쿼리 실패");
				eventReply(socketHelper, createMessage(Bool.FALSE, "서류 삭제 실패"));
			}
		}
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		catch (ClassNotFoundException | IOException e) 
		{
			System.out.println("역직렬화 실패");
			eventReply(socketHelper, createMessage(Bool.FALSE, "해당되는 데이터가 없습니다."));
			return;
		}		
		
	}
	
	//관리자 - 서류 조회 및 제출 - 업로드 버튼 클릭 시
	public static void admin_documentManagePage_onUpload(Protocol protocol, SocketHelper socketHelper)
	{
		//학생으로부터 오프라인으로 받은 서류를 대리제출 하기 위함.
		
		//아래 로직은 student_submitDocumentPage_onSubmit와 거의 동일하다! 
		//학번만 헤더에서 따내느냐, 클라이언트에게서 입력받느냐 차이지. 그 이후로는 똑같으니 코드 재활용하시길!
		//단지 클라이언트에게 다시 알려줄때 page가 달라지거나 할듯.
		
		//1. 서버 컴퓨터 내 저장할 공간에 빈공간이 10MB보다 큰지 확인한다. -> 빈공간이 10MB보다 크면 진행, 작으면 클라이언트에게 안된다고 알려줌.
		//2. 헤더에서 파일 유형이 결핵진단서인지, 서약서인지 확인한다.
		//3. 현재 날짜로부터 학기를 특정한다. (학기는 201901 ~ 201906)
		//4. /파일타입/학기_학번.jpg 와 같은 형식으로 저장된다.
		//	 (학기가 겹치면 덮어씌워진다. 즉, 한학기에 한 파일만 유효함)
		//5. 파일 저장 성공/실패 여부를 클라이언트에게 알려준다.
	}
	
	//관리자 - 서류 조회 및 제출 - UPDATE 버튼 클릭 시
	public static void admin_documentManagePage_onUpdate(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 서류유형, 제출일, 진단일로 서류 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 UPDATE쿼리를 쏜다.
		//	   (유효여부를 클라이언트에게서 받은 T/F로 UPDATE한다)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. UPDATE 쿼리 결과를 클라이언트에게 알려준다.
	}
}

//변경 로그
//2019-12-06 v1.00   
//Responser.java 생성하였음 -명근

//2019-12-07 v1.01
//	주석 추가 및 오타 수정 -명근

//2019-12-07 v1.02
//	관리자 페이지 작업 시작 -명근

//2019-12-07 v1.03
//	관리자 페이지 기본 로직 완성 -명근

//2019-12-07 v1.04
//	사용자 페이지 오타 수정 -명근

//2019-12-08 v1.05
// static붙이고 인자 모두 추가 - 서희

//2019-12-08 v1.06
// 파일 경로에 대한 주석 수정 - ㅅㅁ