import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import DB.ApplicationParser;
import DB.AssignAlgorithm;
import DB.CurrentSemesterParser;
import DB.DormParser;
import DB.ScheduleParser;
import DB.StudentParser;
import enums.Bool;
import enums.Code1;
import enums.Code1.Page;
import enums.Code2;
import enums.Direction;
import enums.Gender;
import enums.ProtocolType;
import models.Application;
import models.Bill;
import models.Dormitory;
import models.Schedule;
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
//		String id = DifferentClass2.checkId();
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
	
	//학생 - 생활관 입사 신청 - 들어왔을 때
	public static void student_submitApplicationPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 입사 신청 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		boolean isAdmissible = ScheduleParser.isAdmissible(Code1.Page.입사신청);
		System.out.println("스케쥴 체크됨");
		if(!isAdmissible)
		{
			//이렇게 튜플로 보내주는 이유는, 아래에서 스케쥴 체크에서 성공했을때 튜플로 보내기 때문임.
			Tuple<String, String> failMessage = new Tuple<String, String>("현재 생활관 입사 신청 기간이 아닙니다.", null);
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(failMessage)).build());
			return;
		}
		
		//2. 받은 요청의 헤더에서 학번을 알아낸다.
		String id = (String) ProtocolHelper.deserialization(protocol.getBody());
		
		//3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
		Gender gender = StudentParser.getGender(id);
		
		//4. 생활관 테이블에서 이번 학기에 해당하고, 성별에 해당하는 기숙사 정보 목록을 가져온다.
		//	 가져와야할 정보는 생활관 테이블의 생활관명, 기간구분(없으면말고), 식사구분, 5일식 식비, 7일식 식비, 관리비,
		int semester;
		semester = CurrentSemesterParser.getCurrentSemester();											//나중에 이런 코드 만들어서 쓰게해야됨.
		ArrayList<Dormitory> dormitoryList = DormParser.getDormitoryList(semester, gender);
		
		//5. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
		String description = ScheduleParser.getDescription(Code1.Page.입사신청);
		
		//6. 해당 정보를 객체화, 배열로 만들어 클라이언트에게 전송한다.
		Tuple<String, ArrayList<Dormitory>> resultTuple = new Tuple(description, dormitoryList);
		
		//전송한다.
		socketHelper.write(new Protocol.Builder(
				ProtocolType.EVENT, 
				Direction.TO_CLIENT, 
				Code1.NULL, 
				Code2.NULL
				).body(ProtocolHelper.serialization(resultTuple)).build());
	}
	
	//학생 - 생활관 입사 신청 - 등록 버튼 클릭 시
	public static void student_submitApplicationPage_onSubmit(Protocol protocol, SocketHelper socketHelper) throws IOException, SQLException, ClassNotFoundException
	{
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		String id = (String) ProtocolHelper.deserialization(protocol.getBody());
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역이 있는지 조회 -> TRUE 이면 내역 취소하고 하라고 클라이언트에게 알려줌. FALSE이면 다음으로
		try {
			if(ApplicationParser.isExist(id))
			{
				socketHelper.write(new Protocol.Builder(
						ProtocolType.EVENT, 
						Direction.TO_CLIENT, 
						Code1.NULL, 
						Code2.NULL
						).body(ProtocolHelper.serialization("이전 신청 정보를 삭제해 주세요.")).build());
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//3. 받은 데이터를 역직렬화한다. ([생활관구분, 기간구분, 식사구분] x4 와 휴대전화번호, 코골이여부가 나옴)
		Application[] A = (Application[])ProtocolHelper.deserialization(protocol.getBody());
		//4. 해당 배열을 신청 데이트에 INSERT한다.
		for(int i = 0; i < A.length; i++)  //(int choice, String mealType, Bool isSnore, String dormitoryName, Gender gender, int semesterCode, String id)
		{
			ApplicationParser.insertApplication(A[i].getChoice(), A[i].getMealType(), A[i].isSnore(), A[i].getDormitoryName(), A[i].getGender(), A[i].getSemesterCode(), id); //이거 풀하고 다시 짤거에요
		}
		//5. 클라이언트에게 성공 여부를 알려준다.(성공/DB연결 오류로 인한 실패/DB사망/알수없는오류 등등...)
		try {
			if(ApplicationParser.isExist(id))
			{
				socketHelper.write(new Protocol.Builder(
						ProtocolType.EVENT, 
						Direction.TO_CLIENT, 
						Code1.NULL, 
						Code2.NULL
						).body(ProtocolHelper.serialization("성공.")).build());
				return;
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//학생 - 생활관 입사 신청 - 취소 버튼 클릭 시 (2019-12-08 명근 수정)
		public static void student_submitApplicationPage_onCancel(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException
		{
			//1. 받은 요청의 헤더에서 학번을 알아낸다. 
			String id = (String) ProtocolHelper.deserialization(protocol.getBody());
			
			//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역이 있는지 조회 -> TRUE 이면 다음으로, FALSE이면 클라이언트에게 내역 없다고 알려줌.
			boolean isExist = false;
			try {
				isExist = ApplicationParser.isExist(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(!isExist)
			{
				//없다고 알려줌
				return;
			}
			
			//-미구현-
			
			//3. DB에 삭제 요청을 한다.
			//4. 클라이언트에게 삭제 성공 여부를 알려준다.
		}
	
	//------------------------------------------------------------------------
	
	//학생 - 생활관 신청 조회 - 들어왔을 때
	public static void student_CheckApplicationPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 신청내역 조회 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		if(ScheduleParser.isAdmissible((Page)protocol.code1))
		{
			//2. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
			String notice = ScheduleParser.getDescription((Page)protocol.code1);
			//3. 스케쥴 객체를 클라이언트에게 전송한다.
			Tuple<Bool,String> resultTuple = new Tuple(Bool.TRUE, notice);
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(resultTuple)).build());
			//(4. 클라이언트에서는 받은 비고(안내사항)을 표시한다)
		}
		else
		{
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization("신청조회기간이 아닙니다.")).build());
		}
		
	}
	
	//학생 - 생활관 신청 조회 - 조회 버튼 클릭 시

	public static void student_CheckApplicationPage_onCheck(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException, SQLException
	{
		ArrayList<Application> applicationResult = new ArrayList<>();
		ArrayList<Application> passedApplicationResult = new ArrayList<>();
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		String id = (String) ProtocolHelper.deserialization(protocol.getBody());
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 지망, 생활관명, 식사구분을 조회.
		applicationResult = ApplicationParser.getApplicationResult(id);
		//	 (클라이언트의 '생활관 입사지원 내역' 테이블뷰에 표시할 것임)
		//3. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 합격여부가 T인 내역의 지망, 생활관명, 식사구분, 합격여부, 납부여부를 조회.
		// 구현중
		passedApplicationResult = ApplicationParser.getPassedApplication(id);
		//	 (클라이언트의 '생활관 선발 결과' 테이블뷰에 표시할 것임)
		//4. 조회된 내역을 객체화, 배열에 담아 클라이언트에게 반환한다.
		Tuple<ArrayList<Application>, ArrayList<Application>> resultTuple = new Tuple(applicationResult, passedApplicationResult);
		socketHelper.write(new Protocol.Builder(
				ProtocolType.EVENT, 
				Direction.TO_CLIENT, 
				Code1.NULL, 
				Code2.NULL
				).body(ProtocolHelper.serialization(resultTuple)).build());
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 생활관 고지서 조회 - 들어왔을 때
	public static void student_CheckBillPage_onEnter(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 스케쥴을 확인하고 고지서 조회 가능한 날짜인지 조회 -> TRUE이면 괜찮다고 클라이언트에게 전송, FALSE이면 못들어가게 막음
	}
	
	//학생 - 생활관 고지서 조회 - 조회 버튼 클릭 시
	public static void student_CheckBillPage_onCheck(Protocol protocol, SocketHelper socketHelper) throws ClassNotFoundException, IOException, SQLException
	{
		int cost;
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		String id = (String) ProtocolHelper.deserialization(protocol.getBody()); //이 부분 확인해주세요
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 합격여부가 T인 내역 조회 -> 내역 있으면 다음으로, 없으면 없다고 클라이언트에게 알려줌
		try {
			if(!ApplicationParser.isExistPassState(id))
			{
				socketHelper.write(new Protocol.Builder(
						ProtocolType.EVENT, 
						Direction.TO_CLIENT, 
						Code1.NULL, 
						Code2.NULL
						).body(ProtocolHelper.serialization("고지서 내역이 없습니다.")).build());
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//3. 합격한 신청 내역에 관한 납부해야 할 비용을 저장
		cost = DormParser.getCheckBillCost(id);
		//4. 랜덤 생성한 계좌번호와, 은행 명, 계산한 비용을 객체화해서 클라이언트에게 전송한다.
		Random rand = new Random();
		int accountNum = rand.nextInt(100)+1000;
		Bill bill = new Bill("농협",accountNum,cost);
		socketHelper.write(new Protocol.Builder(
				ProtocolType.EVENT, 
				Direction.TO_CLIENT, 
				Code1.NULL, 
				Code2.NULL
				).body(ProtocolHelper.serialization(bill)).build());
		//(. 클라이언트는 이걸 받아서 대충 메모장으로 띄워준다.)
	}
	//------------------------------------------------------------------------
	
	//학생 - 생활관 호실 조회 - 들어왔을 때
	public static void student_checkRoomPage_onEnter(Protocol protocol, SocketHelper socketHelper) throws Exception
	{
		//1. 스케쥴을 확인하고 호실 조회 가능한 날짜인지 조회 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		if(ScheduleParser.isAdmissible((Page)protocol.code1))
		{
			//2. 스케쥴 테이블에서 비고(안내사항)를 가져온다.
			String notice = ScheduleParser.getDescription((Page)protocol.code1);
			//3. 스케쥴 객체를 클라이언트에게 전송한다.
			Tuple<Bool,String> resultTuple = new Tuple(Bool.TRUE, notice);
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(resultTuple)).build());
			//(4. 클라이언트에서는 받은 비고(안내사항)을 표시한다)
		}
		else
		{
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization("신청조회기간이 아닙니다.")).build());
		}
	}
	
	//학생 - 생활관 호실 조회 - 조회 버튼 클릭 시
	public static void student_checkRoomPage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		///////////////////제가 만드는중 ★ ㅡ서희ㅡ////////////////////////////
		//1. 받은 요청의 헤더에서 학번을 알아낸다. 
		//2. 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 최종합격여부가 T인 내역 조회 
		//3-1. 내역이 없는 경우 불합격이라고 클라이언트에게 알려준다.(객체 만들던지, String 보내던 알아서 해야될듯. 전용 객체가 있는게 바람직하겠다.)
		//3-2. 내역이 있는 경우 신청 테이블에서 최종합격여부, 납부여부, 식비구분, 생활관, 호실유형(이건 일반실 고정)을 조회한다.
		//4. 배정내역 테이블에서 해당 학번이 배정되있는 호실과 침대번호를 가져온다.
		//5. 3-2와 4를 합쳐 객체화한다. 그리고 이것을 클라이언트에게 전송한다.
		//(6. 클라이언트는 받은 객체를 역직렬화, UI에 표기한다)
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 서류 제출 - 들어왔을 때
	public static void student_submitDocumentPage_onEnter(Protocol protocol, SocketHelper socketHelper)
	{
		//실제 원스톱을 기반으로, 학생이 서류 제출하는건 아무때나 할 수 있다고 하였다.
		//1. 서류 유형을 객체화 배열화하여 클라이언트로 전송한다.
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
		//(2. 클라이언트는 받은 배열을 역직렬화하여 서류유형 combobox에 표시한다)
	}
	
	//학생 - 서류 조회 - 조회 버튼 클릭 시
	public static void student_checkDocumentPage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 받은 요청의 헤더에서 학번, 서류유형을 알아낸다. 
		//2. 서류 테이블에서 해당 학번이 이번 학기에 제출한 내역 중 서류유형이 일치하는 것을 찾는다. -> 있으면 진행, 없으면 없다고 알려줌
		//3. 서류 테이블에서 서류유형, 제출일시, 진단일시, 파일경로를 알아내어 객체화한다.
		//4. 클라이언트에게 전송한다.
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
		Schedule[] schedule = ScheduleParser.getAllSchedule();
		//2. 객체 배열을 직렬화하여 클라이언트로 전송한다.
		socketHelper.write(new Protocol.Builder(
				ProtocolType.EVENT, 
				Direction.TO_CLIENT, 
				Code1.NULL, 
				Code2.NULL
				).body(ProtocolHelper.serialization(schedule)).build());
		//(3. 클라이언트는 등록 gridView 안의 유형 combobox에 값을 채워준다.)
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_scheduleManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 스케쥴 할일 코드 테이블에서 목록을 객체로 만들어 배열로 가져온다. (ID, 할일코드, 시작일, 종료일, 비고)
		//2. 스케쥴 테이블에서 목록을 객체로 만들어 배열로 가져온다. (코드, 이름)
		//3. 스케쥴 객체 배열을 클라이언트로 전송한다.
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
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 생활관 조회 및 관리 - 들어왔을 때
	public static void admin_dormitoryManagePage_onEnter(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 식사의무 ENUM을 배열화해서 목록을 만든다.
		//2. 배열화한 목록을 직렬화해서 클라이언트로 전송한다.
		//(3. 클라이언트는 받은 ENUM 배열을 역직렬화하여 식사의무 combobox에 표시한다)
		
		//[ENUM 배열화 예시]
		//arrayList<MealDuty> data = new arrayList<MealDuty>(MealDuty.NOMEAL, MealDuty.MEAL5, MealDuty.MEAL7);
	}
	
	//관리자 - 생활관 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_dormitoryManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 생활관 정보 테이블에서 모든 정보를 가져와 객체화한다. (생활관명, 학기, 수용인원, 식사의무, 5일식 식비, 7일식 식비, 기숙사비
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
	}
	
	//관리자 - 생활관 조회 및 관리 - 등록 버튼 클릭 시
	public static void admin_dormitoryManagePage_onInsert(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트에게서 생활관명, 학기, 수용인원, 식사의무, 5일식 식비, 7일식 식비, 기숙사비를 받는다.
		//2. 생활관 정보 테이블에서 생활관명, 학기로 탐색, 중복되는 값이 있는지 체크한다.
		//3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다.
		//3-2. 기존 값이 존재하지 않으면 INSERT한다.
		//4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc)
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
		boolean isSucceed = true;
		try {
			logic.ResidentSelecter.selectionByChoice();
		} catch (ClassNotFoundException | SQLException e1) {
			isSucceed = false;
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//이것만 부르면 1, 2, 3, 4 전부 처리함
				//		1) 신청 내역에서 생활관별로 묶는다. - 정확히는 생활관별, 지망별로 묶어서 생활관별 0지망 -> 생활관별 1지망 -> 생활관별 2지망 -> 생활관별 3지망 순으로 뽑음
				//		2) 학생 한명당 평균성적을 계산한다.
				//		3) 평균성적순으로 정렬한다. - 정렬하는동안 그 학번으로 이미 합격한 신청이 있는지 확인하고 이미 있으면 정렬 대상에서 제외함
				//		4) 남은 자리가 n이면 n명까지 합격여부를 Y로 UPDATE - 맞워요
				//3. 결과를 클라이언트에게 알려준다(성공/실패?) - 이건 어케하노
		Tuple<Bool,String> result;
		if(isSucceed)
			result = new Tuple<Bool,String>(Bool.TRUE, "성공했습니다");
		else
			result = new Tuple<Bool,String>(Bool.FALSE, "실패했습니다");
		
		try {
			
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(result)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_selecteesManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 신청 테이블에서 이번 학기 신청 목록을 가져와 객체화한다. (학번, 생활관명, 학기, 지망, 몇일식, 납부여부, 합격여부, 최종결과, 코골이여부)
		//   (합격여부 Y, N인거 관계없이 가져와야될듯. 그래야 사실상 여기서 관리자가 신청내역 조회가능함)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		try {
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(DB.ApplicationParser.getAllApplications())).build());
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 삭제 버튼 클릭 시
	public static void admin_selecteesManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		Bool isSucceed = Bool.TRUE;
		//1. 클라이언트로부터 받은 학번, 생활관명, 학기, 지망으로 신청 테이블에서 조회한다.
		Application temp;
		try {
			temp = (Application) ProtocolHelper.deserialization(protocol.getBody());
			DB.ApplicationParser.deleteApplication(temp);
		} catch (ClassNotFoundException | IOException | SQLException e1) {
			// TODO Auto-generated catch block
			isSucceed = Bool.FALSE;
			e1.printStackTrace();
		}
		
		Tuple<Bool,String> result;
		if(isSucceed == Bool.TRUE)
			result = new Tuple<Bool,String>(Bool.TRUE, "성공했습니다");
		else
			result = new Tuple<Bool,String>(Bool.FALSE, "실패했습니다");
		
		try {
			
			socketHelper.write(new Protocol.Builder(
					ProtocolType.EVENT, 
					Direction.TO_CLIENT, 
					Code1.NULL, 
					Code2.NULL
					).body(ProtocolHelper.serialization(result)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
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
		AssignAlgorithm.batchStart();
		
		//3. 결과를 클라이언트에게 알려준다(성공/실패?)
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 조회 버튼 클릭 시
	public static void admin_boarderManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 배정내역 테이블에서 이번 학기 배정내역 목록을 가져와 객체화한다. (학번, 호, 학기, 생활관명, 자리, 퇴사예정일)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 삭제 버튼 클릭 시
	public static void admin_boarderManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 호, 학기, 생활관명으로 배정내역 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//	   (신청 테이블에서 최종합격여부를 N으로 UPDATE해야할지는 모르겠음...)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 등록 버튼 클릭 시
	public static void admin_boarderManagePage_onInsert(Protocol protocol, SocketHelper socketHelper)
	{
		//배정내역에 학생을 임의로 추가하기 위한 기능
		//배정내역에 학생을 넣고, 신청 테이블에도 몇일식인지, 코골이여부를 기록하기 위해 INSERT해야됨.
		
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
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 납부 여부 조회 및 관리 - 업데이트 버튼 클릭 시
	public static void admin_paymentManagePage_onUpdate(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 생활관명, 학기로 납부여부 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 UPDATE쿼리를 쏜다.
		//	   (납부여부를 클라이언트에게서 받은 T/F로 UPDATE한다)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. UPDATE 쿼리 결과를 클라이언트에게 알려준다.
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
	public static void admin_documentManagePage_onEnter(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 서류유형 ENUM을 배열화해서 목록을 만든다.
		//2. 배열화한 목록을 직렬화해서 클라이언트로 전송한다.
		//(3. 클라이언트는 받은 ENUM 배열을 역직렬화하여 서류유형 combobox에 표시한다)
		
		//[ENUM 배열화 예시]
		//arrayList<DocumentType> data = new arrayList<DocumentType>(DocumentType.MEDICAL, DocumentType.OATH);
	}
	
	//관리자 - 서류 조회 및 제출 - 조회 버튼 클릭 시
	public static void admin_documentManagePage_onCheck(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 서류 테이블에서 이번 학기 서류제출내역 목록을 가져와 객체화한다. (학번, 서류유형, 제출일, 진단일, 서류저장경로, 유효여부)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
	}
	
	//관리자 - 서류 조회 및 제출 - 삭제 버튼 클릭 시
	public static void admin_documentManagePage_onDelete(Protocol protocol, SocketHelper socketHelper)
	{
		//1. 클라이언트로부터 받은 학번, 서류유형, 제출일로 서류 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
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