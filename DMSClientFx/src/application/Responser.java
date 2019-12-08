package application;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;

import enums.*;
import enums.Code1.Page;
import enums.Code2.FileCode;
import models.*;
import utils.*;

//디버깅용 클래스
//대충 클라이언트에서 어떤 요청을 했을때 그에 대한 반응(로직)을 모아둠.
//주석으로 로직을 설명하고, 실제 동작가능한 코드도 넣어라.
//실제 네트워킹이 정상적으로 작동하면, 이 클래스를 삭제하고 코드를 적절한 위치에 옮기도록.
//로직을 변경하게 될 경우 맨 아래 주석에 로그를 남겨주세요!!!

//학기코드
//201901 : 1학기
//201902 : 여름계절
//201903 : 여름계절이후까지
//201904 : 2학기
//201905 : 겨울계절
//201906 : 겨울계절이후까지

public class Responser
{
	//아래 규칙을 지켜주세요.
	//(1) 메소드 위에 붙는 모든 주석은 클라이언트에서의 동작이다.
	//(2) 메소드 안에 있는 주석 및 코드는 클라이언트에서의 동작이다.
	//(3) 괄호가 쳐진 인덱스와 내용은 서버에서의 동작이다.
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
	
	//-------------------------------------------------------------------------------
	//-------------------------------------로직--------------------------------------
	//-------------------------------------------------------------------------------
	
	//학생 - 생활관 입사 신청 - 들어왔을 때 (2019-12-08 명근 수정)
	public static Tuple<String, ArrayList<Dormitory>> student_submitApplicationPage_onEnter() 
	{
		//1. 입사 신청 가능한 날짜인지 서버에게 물어본다 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		//프로토콜 빌더를 사용해서 입사신청에 들어왔다고, 현 계정을 함께 보낸다(학번을 전달하기 위해)
        //2. 서버로 요청 및 응답을 받는다.
        //3. 클라이언트는 받은 안내사항 + 생활관목록을 표시한다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사신청, Code2.Event.REFRESH, UserInfo.account);
		Tuple<String, ArrayList<Dormitory>> resultTuple = (Tuple<String, ArrayList<Dormitory>>) sendAndReceive(protocol);
        return resultTuple;
	}
	
	//학생 - 생활관 입사 신청 - 등록 버튼 클릭 시 (2019-12-08 명근 수정)
	public static Tuple<Bool, String> student_submitApplicationPage_onSubmit(ArrayList<Application> applicationList)
	{
		//1. 사용자가 선택한 신청을 배열화한다.
		//	 사용자 정보와 신청배열을 보내야하기때문에 튜플 사용함.
		//2. 데이터를 보내기위해 프로토콜을 만든다.
		//3. 데이터를 서버로 보내고 성공여부, 메시지를 받는다.
		//4. 서버에서 받은 성공여부로 메세지를 표시한다.
		
		Tuple<Account, ArrayList<Application>> sendData = new Tuple<Account, ArrayList<Application>>(UserInfo.getInstance().account, applicationList);
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사신청, Code2.Event.SUBMIT, sendData);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//학생 - 생활관 입사 신청 - 취소 버튼 클릭 시 (2019-12-08 명근 수정)
	public static Tuple<Bool, String> student_submitApplicationPage_onCancel()
	{
		//1. 서버에게 입사 신청 취소요청을 한다.
		//(2. 서버는 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역이 있는지 조회)
		//(3. TRUE 이면 됬다고 클라이언트에게 알려줌, FALSE이면 클라이언트에게 내역 없다고 알려줌.)
		//4. 서버에서 받은 성공여부, 메세지를 표시한다.		
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사신청, Code2.Event.CANCEL, UserInfo.account);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
        return result;
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 생활관 신청 조회 - 들어왔을 때 (2019-12-08 명근 수정)
	public static Tuple<Bool, String> student_CheckApplicationPage_onEnter()
	{
		//1. 신청내역 조회 가능한 날짜인지 서버에게 물어본다 -> TRUE이면 안내사항을, FALSE이면 못들어가게 막고 실패원인 메시지도 전달
		//(2. 서버는 스케쥴 테이블에서 비고(안내사항)를 가져온다.)
		//(3. 서버는 스케쥴 객체를 클라이언트에게 전송한다.)
		//4. 서버에서 받은 진입여부와 메시지를 표시한다.(TRUE이면 안내사항을, FALSE이면 못들어가게 막고 실패원인 메시지도 전달)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.신청조회, Code2.Event.REFRESH, null);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		
		return result;
	}
	
	//학생 - 생활관 신청 조회 - 조회 버튼 클릭 시 (2019-12-08 명근 수정)
	public static Tuple<ArrayList<Application>, ArrayList<Application>> student_CheckApplicationPage_onCheck()
	{
		//1. 서버에게 생활관 신청 조회 요청을 한다. 
		//(2. 서버는 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 지망, 생활관명, 식사구분을 조회.
		//	  '생활관 입사지원 내역' 테이블뷰에 표시할 것임)
		//(3. 서버는 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 합격여부가 T인 내역의 지망, 생활관명, 식사구분, 합격여부, 납부여부를 조회.
		//	  '생활관 선발 결과' 테이블뷰에 표시할 것임)
		//(4. 조회된 내역을 객체화, 배열에 담아 클라이언트에게 반환한다.)
		//5. 서버로부터 받은 내역을 각각 두개의 테이블에 표시한다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.신청조회, Code2.Event.CHECK, UserInfo.getInstance().account);
		Tuple<ArrayList<Application>, ArrayList<Application>> result = (Tuple<ArrayList<Application>, ArrayList<Application>>) sendAndReceive(protocol);
		return result;
	}
	
	//------------------------------------------------------------------------
	 
	//학생 - 생활관 고지서 조회 - 들어왔을 때 (2019-12-08 명근 수정)
	public static Tuple<Bool, String> student_CheckBillPage_onEnter()
	{
		//1. 고지서 조회 가능한 날짜인지 서버에게 물어본다 -> TRUE이면 진행, FALSE이면 못들어가게 막고 실패원인 메시지도 전달
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.고지서조회, Code2.Event.REFRESH, null);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//학생 - 생활관 고지서 조회 - 조회 버튼 클릭 시 (2019-12-08 명근 수정)
	public static String student_CheckBillPage_onCheck()
	{
		//1. 서버에게 생활관 고지서 조회 요청을 한다.
		//(2. 서버는 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 합격여부가 T인 내역 조회 -> 내역 있으면 다음으로, 없으면 없다고 클라이언트에게 알려줌)
		//(3. 서버는 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 합격여부가 T인 내역의 식사구분, 생활관구분을 알아낸다.)
		//(4. 서버는 해당 생활관, 해당 식비로 총 금액을 알아낸다.)
		//(5. 서버는 랜덤 생성한 계좌번호와, 은행 명, 계산한 식비를 객체화해서 클라이언트에게 전송한다.)
		//6. 서버가 보낸 정보를 받아서 대충 메모장으로 띄워준다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.고지서조회, Code2.Event.CHECK, UserInfo.getInstance().account);
		String result = (String) sendAndReceive(protocol);
		return result;
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 생활관 호실 조회 - 들어왔을 때 (2019-12-08 명근 수정)
	public static Tuple<Bool, String> student_checkRoomPage_onEnter()
	{
		//1. 호실 조회 가능한 날짜인지 서버에게 물어본다 -> TRUE이면 다음으로, FALSE이면 못들어가게 막음
		//(2. 서버는 스케쥴 테이블에서 비고(안내사항)를 가져온다.)
		//(3. 서버는 스케쥴 객체를 클라이언트에게 전송한다.)
		//4. 서버로부터 받은 비고(안내사항)을 표시한다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.호실조회, Code2.Event.REFRESH, UserInfo.getInstance().account);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//학생 - 생활관 호실 조회 - 조회 버튼 클릭 시 (2019-12-08 명근 수정)
	public static Tuple<Application, PlacementHistory> student_checkRoomPage_onCheck()
	{
		//1. 서버에게 생활관 호실 조회 요청을 한다.   
		//(2. 서버는 신청 테이블에서 해당 학번이 이번 학기에 신청한 내역 중 최종합격여부가 T인 내역 조회) 
		//(3-1. 서버는 내역이 없는 경우 불합격이라고 클라이언트에게 알려준다.)
		//(3-2. 서버는 내역이 있는 경우 신청 테이블에서 최종합격여부, 납부여부, 식비구분, 생활관, 호실유형(이건 일반실 고정)을 조회한다.)
		//(4. 서버는 배정내역 테이블에서 해당 학번이 배정되있는 호실과 침대번호를 가져온다.)
		//(5. 서버는 3-2와 4를 합쳐 객체화한다. 그리고 이것을 클라이언트에게 전송한다.)
		//6. 서버로부터 받은 신청, 배정내역을 역직렬화, UI에 표기한다
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.호실조회, Code2.Event.CHECK, UserInfo.getInstance().account);
		Tuple<Application, PlacementHistory> result = (Tuple<Application, PlacementHistory>) sendAndReceive(protocol);
		return result;
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 서류 제출 - 들어왔을 때 (2019-12-08 명근 수정)
	public static ArrayList<Code1.FileType> student_submitDocumentPage_onEnter()
	{
		//실제 원스톱을 기반으로, 학생이 서류 제출하는건 아무때나 할 수 있다고 하였다.
		//1. 서버에게 서류 제출 페이지 들어왔다고 알려준다.
		//(2. 서버는 서류 유형을 객체화 배열화하여 클라이언트로 전송한다.)
		//3. 서버로부터 받은 배열을 역직렬화하여 서류유형 combobox에 표시한다
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류제출, Code2.Event.REFRESH, null);
		ArrayList<Code1.FileType> result = (ArrayList<Code1.FileType>) sendAndReceive(protocol);
		return result;
	}
	
	//학생 - 서류 제출 - 제출 버튼 클릭 시(파일 업로드) (2019-12-08 명근 수정)
	public static Bool student_submitDocumentPage_onSubmit(Code1.FileType fileType, File file) throws Exception {
		//이대로 보내면 누가보내는지 모른다!!!
		//1. 서버로 파일을 업로드한다.
		//(2. 서버는 컴퓨터 내 저장할 공간에 빈공간이 10MB보다 큰지 확인한다. -> 빈공간이 10MB보다 크면 진행, 작으면 클라이언트에게 안된다고 알려줌.)
		//(3. 서버는파일 저장 성공/실패 여부를 클라이언트에게 알려준다.)
		
		//3. 결과를 메시지로 띄운다.
		// TODO 성공 실패만 반환한다. 테스트는 안해봤는데 될것같음.
		Tuple<String, byte[]> idWithFile = new Tuple<>(UserInfo.account.accountId, Files.readAllBytes(file.toPath()));
		Protocol protocol = new Protocol
				.Builder(ProtocolType.FILE, Direction.TO_SERVER, fileType, FileCode.UPLOAD)
				.body(ProtocolHelper.serialization(idWithFile))
				.build();
		protocol = SocketHandler.INSTANCE.request(protocol);

		if (FileCode.SUCCESS == (Code2.FileCode)protocol.code2)
			return Bool.TRUE;
		else
			return Bool.FALSE;
	}
	
	//------------------------------------------------------------------------
	
	//학생 - 서류 조회 - 들어왔을 때 (2019-12-08 명근 수정)
	public static ArrayList<Code1.FileType> student_checkDocumentPage_onEnter()
	{
		//1. 서버에게 서류 조회 페이지 들어왔다고 알려준다.
		//(2. 서버는 서류 유형을 객체화 배열화하여 클라이언트로 전송한다.)
		//3. 받은 배열을 역직렬화하여 서류유형 combobox에 표시한다
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류조회, Code2.Event.REFRESH, null);
		ArrayList<Code1.FileType> result = (ArrayList<Code1.FileType>) sendAndReceive(protocol);
		return result;
	}
	
	//학생 - 서류 조회 - 조회 버튼 클릭 시 (2019-12-08 명근 수정)
	public static Tuple<Bool, Document> student_checkDocumentPage_onCheck(Code1.FileType fileType)
	{
		//1. 서버에게 서류 조회 요청을 한다.(서류유형을 보내서, 어떤 서류를 조회하려는지 알려준다)
		//(2. 서버는 서류 테이블에서 해당 학번이 이번 학기에 제출한 내역 중 서류유형이 일치하는 것을 찾는다. -> 있으면 진행, 없으면 없다고 알려줌)
		//(3. 서버는 서류 테이블에서 서류유형, 제출일시, 진단일시, 파일경로를 알아내어 전송한다.)
		//4. 제출일시, 진단일시, 파일경로를 표시한다.
		
		//어느 파일을 조회하려는지와 학번(계정)을 보낸다.
		Tuple <Account, Code1.FileType> data = new Tuple<Account, Code1.FileType>(UserInfo.getInstance().account, fileType);
		
		//프로토콜 생성
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류조회, Code2.Event.CHECK, data);
		
		//통신하고 받았을 때 Bool이 True이면 정상적인 Document, False이면 서류 내역이 존재하지 않음.
		//객체가 null이면 통신실패다. 즉, Bool=True -> 성공, Bool=False -> 내역없음, null=통신실패
		Tuple<Bool, Document> result = (Tuple<Bool, Document>) sendAndReceive(protocol);
		
		return result;
	}
	
	//학생 - 서류 조회 - 다운로드 버튼 클릭 시(파일 다운로드) (2019-12-08 명근 수정)
	public static File student_checkDocumentPage_onDownlaod(Code1.FileType fileType)
	{
		//1. 남은 용량이 10MB 이상인지 체크한다 -> 10MB 이상이면 다음, 10MB 이하면 중단
		//2. 서버에게 서류 조회 요청을 한다.(파일경로를 보내서, 어떤 파일을 다운하려는지 알려준다)
		//(3. 서버는 해당 파일 경로로 파일을 찾는다. -> 파일이 있으면 진행, 없으면 없다고 알려줌(이땐 버그라고 봐야할듯))
		//(4. 서버는 파일을 찾았으면 클라이언트에 업로드한다.)
		//5. 다운로드 받은 파일을 대충 바탕화면에 저장하고 연다.
		
		//파일 다운로드 요청 프로토콜 생성
		Protocol requestProtocol = fileProtocolBuilder(fileType, FileCode.REQUEST, UserInfo.getInstance().account);
		
		//응답 받음. 반환값이 null이면 통신 실패, Bool이 True면 OK, False이면 거절, 사유는 String에. 
		Tuple<Bool, String> requestResult = (Tuple<Bool, String>) sendAndReceive(requestProtocol);
		
		if(requestResult == null)
			return null;
		
		if(requestResult.obj1 == Bool.FALSE)
		{
			//TODO : 여기는 거절인데, 거절사유를 넘겨주던가 해야됨. 함수 분리하는게 나을지도.
			String rejectMsg = requestResult.obj2;
			return null;
		}
		
		//파일 다운로드 프로토콜 생성
		Protocol fileProtocol = fileProtocolBuilder(fileType, FileCode.REQUEST, null);
		
		//반환값이 null이면 에러임.
		File result = (File) sendAndReceive(requestProtocol);
		return result;
	}
	
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	
	//관리자 - 선발 일정 조회 및 관리 - 들어왔을 때
	public static ArrayList<ScheduleCode> admin_scheduleManagePage_onEnter()
	{
		//1. 서버에게 선발 일정 조회 및 관리 들어왔다고 알려준다.
		//(2. 서버는 스케쥴 유형을 객체화 배열화하여 클라이언트로 전송한다.)
		//3. 받은 배열을 역직렬화하여 스케쥴유형 combobox에 표시한다
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.선발일정관리, Code2.Event.REFRESH, null);
		ArrayList<ScheduleCode> result = (ArrayList<ScheduleCode>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 조회 버튼 클릭 시
	public static ArrayList<Schedule> admin_scheduleManagePage_onCheck()
	{
		//1. 서버에게 서류 조회 요청을 한다.(요청만 보낸다)
		//(2. 서버는 스케쥴 테이블에서 목록을 객체로 만들어 배열로 가져온다.)
		//(3. 서버는 스케쥴 객체 배열을 전송한다)
		//4. 클라이언트는 받아서 tableView에 표시한다. 클라이언트에는 ID, 할일이름, 시작일, 종료일, 비고가 표시된다
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.선발일정관리, Code2.Event.CHECK, null);
		ArrayList<Schedule> result = (ArrayList<Schedule>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 삭제 버튼 클릭 시
	public static Tuple<Bool, String> admin_scheduleManagePage_onDelete(String scheduleId)
	{
		//1. 서버에게 스케쥴 ID와 함께 삭제 요청을 보낸다.
		//(2. 스케쥴 테이블에서 해당 ID로 탐색한다.)
		//(3-1. DB에 해당 ID가 존재한다. -> DB에 DELETE 요청)
		//(3-2. DB에 해당 ID가 존재하지 않는다. -> 없다고 클라이언트에게 알려줌.)
		//(4. DELETE 요청 결과를 클라이언트에게 알려준다. (성공/실패/아마존 사망...etc))
		//5. 결과값을 받아 메시지로 띄워준다.
		
		//Bool이 True면 성공, False면 실패, String에는 각각 메시지
		Protocol protocol = eventProtocolBuilder(Code1.Page.선발일정관리, Code2.Event.DELETE, scheduleId);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 선발 일정 조회 및 관리 - 등록 버튼 클릭 시
	public static Tuple<Bool, String> admin_scheduleManagePage_onInsert(Schedule schedule)
	{
		//1. 유형코드, 시작일, 종료일, 비고를 서버로 전송한다. (유형은 코드로 전달되어야 한다. String으로 전달받는건 미개함
		//	 그래서 유형 이름을 받는게 아니라 유형 코드를 받는것)
		//(2. 스케쥴 테이블에서 유형, 시작일, 종료일이 중복되는지 체크한다.)
		//(3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다 )
		//(3-2. 기존 값이 존재하지 않으면 INSERT한다.)
		//(4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc))
		//5. 성공/실패여부를 알려준다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.선발일정관리, Code2.Event.SUBMIT, schedule);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 생활관 조회 및 관리 - 조회 버튼 클릭 시
	public static ArrayList<Dormitory> admin_dormitoryManagePage_onCheck()
	{
		//(1. 생활관 정보 테이블에서 모든 정보를 가져와 객체화한다. (생활관명, 학기, 수용인원, 식사의무, 5일식 식비, 7일식 식비, 기숙사비)
		//(2. 배열화한다.)
		//(3. 직렬화해서 클라이언트에 전송한다.)
		//4. 클라이언트는 받은 배열을 tableView에 표시한다
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.생활관관리, Code2.Event.CHECK, null);
		ArrayList<Dormitory> result = (ArrayList<Dormitory>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 생활관 조회 및 관리 - 삭제 버튼 클릭 시
	public static Tuple<Bool, String> admin_dormitoryManagePage_onDelete(String dormName, String semester)
	{
		//1. 클라이언트로부터 받은 생활관명, 학기로 생활관 정보 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
		
		Tuple<String, String> data = new Tuple<String, String>(dormName, semester);
		Protocol protocol = eventProtocolBuilder(Code1.Page.생활관관리, Code2.Event.DELETE, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 생활관 조회 및 관리 - 등록 버튼 클릭 시
	public static Tuple<Bool, String> admin_dormitoryManagePage_onInsert(Dormitory data)
	{
		//1. 클라이언트에게서 생활관명, 학기, 수용인원, 식사의무, 5일식 식비, 7일식 식비, 기숙사비를 받는다.
		//2. 생활관 정보 테이블에서 생활관명, 학기로 탐색, 중복되는 값이 있는지 체크한다.
		//3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다.
		//3-2. 기존 값이 존재하지 않으면 INSERT한다.
		//4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.생활관관리, Code2.Event.SUBMIT, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 입사 선발자 조회 및 관리 - 입사자 선발 버튼 클릭 시
	public static Tuple<Bool, String> admin_selecteesManagePage_onSelection()
	{
		//입사자 선발 버튼은 신청 목록에서 합격여부를 Y로 바꾸는 역할을 한다
		//ex) 100명을 뽑아야되면 성적순으로 정렬 뒤 총 신청자 중에서 상위 100명까지 합격여부를 Y로 바꾼다?
		//이건 선발 짜놓은 친구들이 구현해놨으니 거기에 맞게 로직 고치면 됨.(로직 주석 구체적으로 달아주셈, 어떻게 돌아가는지 다른애들도 알수있게)
		
		//1. 클라이언트에게 입사자 선발 요청을 받는다. (바디에는 딱히 아무것도 없다. 요청을 위한 통신)
		//2. 선발 알고리즘을 시행한다. (대충 생각해본것임. 더 나은 알고리즘, 이미 구현한 알고리즘 사용해도 됨. 그 경우 아래 알고리즘을 고쳐주셈)
		//		1) 신청 내역에서 생활관별로 묶는다.
		//		2) 학생 한명당 평균성적을 계산한다.
		//		3) 평균성적순으로 정렬한다.
		//		4) 남은 자리가 n이면 n명까지 합격여부를 Y로 UPDATE
		//3. 결과를 클라이언트에게 알려준다(성공/실패?)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사선발자관리, Code2.Event.SELECTION, null);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 조회 버튼 클릭 시
	public static ArrayList<Application> admin_selecteesManagePage_onCheck()
	{
		//1. 신청 테이블에서 이번 학기 신청 목록을 가져와 객체화한다. (학번, 생활관명, 학기, 지망, 몇일식, 납부여부, 합격여부, 최종결과, 코골이여부)
		//   (합격여부 Y, N인거 관계없이 가져와야될듯. 그래야 사실상 여기서 관리자가 신청내역 조회가능함)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사선발자관리, Code2.Event.CHECK, null);
		ArrayList<Application> result = (ArrayList<Application>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 삭제 버튼 클릭 시
	public static Tuple<Bool, String> admin_selecteesManagePage_onDelete(Application data)
	{
		//1. 클라이언트로부터 받은 학번, 생활관명, 학기, 지망으로 신청 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사선발자관리, Code2.Event.DELETE, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 입사자 조회 및 관리 - 입사자 등록(배정) 버튼 클릭 시
	public static Tuple<Bool, String> admin_boarderManagePage_onAllocate()
	{
		//입사자 등록(배정) 버튼은 신청 목록에서 합격여부를 Y, 납부내역 Y, 결핵진단서 Y인 신청의 최종합격여부를 Y로 바꾼다.
		//그리고나서 배정내역에 최종합격여부가 Y인 학생들을 배정한다.
		//이것도 로직 짜놓은 친구들이 구현해놨으니 거기에 맞게 로직 고치면 됨.(로직 주석 구체적으로 달아주셈, 어떻게 돌아가는지 다른애들도 알수있게)
		
		//1. 클라이언트에게 입사자 등록(배정) 요청을 받는다. (바디에는 딱히 아무것도 없다. 요청을 위한 통신)
		
		//2. 등록(배정) 알고리즘을 시행한다.
		//   등록 배정 알고리즘 상세설명(대충 생각해본것임. 더 나은 알고리즘, 이미 구현한 알고리즘 사용해도 됨. 그 경우 아래 알고리즘을 고쳐주셈)
		//		1) 신청 테이블에서 이번학기에 합격여부 Y, 납부내역 Y인 것 중 학번, 코골이여부 등을 가져온다.
		//		2) 서류 테이블에서 결핵진단서 유효여부가 Y인걸 체크해서, 위에서 가져온것과 조인.
		//		3) 같은 생활관끼리 신청내역을 묶는다?
		//		4) 배정이 된 학생들은 최종합격여부를 Y로 고친다?
		//		5) 배정내역 알고리즘(배정내역 테이블에 학생 한명 한명 INSERT ?)을 돌린다.
		
		//3. 결과를 클라이언트에게 알려준다(성공/실패?)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사자관리, Code2.Event.ASSIGN, null);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 조회 버튼 클릭 시
	public static ArrayList<PlacementHistory> admin_boarderManagePage_onCheck()
	{
		//1. 배정내역 테이블에서 이번 학기 배정내역 목록을 가져와 객체화한다. (학번, 호, 학기, 생활관명, 자리, 퇴사예정일)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사자관리, Code2.Event.CHECK, null);
		ArrayList<PlacementHistory> result = (ArrayList<PlacementHistory>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 삭제 버튼 클릭 시
	public static Tuple<Bool, String> admin_boarderManagePage_onDelete(PlacementHistory data)
	{
		//1. 클라이언트로부터 받은 학번, 호, 학기, 생활관명으로 배정내역 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//	   (신청 테이블에서 최종합격여부를 N으로 UPDATE해야할지는 모르겠음...)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사자관리, Code2.Event.DELETE, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 입사 선발자 조회 및 관리 - 등록 버튼 클릭 시
	public static Tuple<Bool, String> admin_boarderManagePage_onInsert(Tuple<PlacementHistory, Application> data)
	{
		//배정내역에 학생을 임의로 추가하기 위한 기능
		//배정내역에 학생을 넣고, 신청 테이블에도 몇일식인지, 코골이여부를 기록하기 위해 INSERT해야됨.
		
		//1. 클라이언트에게서 학번, 호, 학기, 생활관명, 자리, 퇴사예정일, 몇일식, 코골이여부를 받는다.
		//2. 배정내역 테이블에서 학번, 호, 학기, 생활관명으로 중복되는 값이 있는지 체크한다.
		//3-1. 기존 값이 존재하면 기존 값 삭제하라고 클라이언트에게 알려준다.
		//3-2. 기존 값이 존재하지 않으면 INSERT한다.
		//	   신청 테이블에도 몇일식, 코골이여부 넣어주기위해 INSERT해줘야 한다.
		//4. INSERT 수행에 대한 결과를 클라이언트에게 알려준다 (성공/실패/아마존사망...etc)
		Protocol protocol = eventProtocolBuilder(Code1.Page.입사자관리, Code2.Event.SUBMIT, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//-------------------------------------------------------------------------
	
	//관리자 - 납부 여부 조회 및 관리 - 조회 버튼 클릭 시
	public static ArrayList<Application> admin_paymentManagePage_onCheck()
	{
		//1. 신청 테이블에서 이번 학기 신청 목록을 가져와 객체화한다. (학번, 생활관명, 학기, 지망, 몇일식, 납부여부, 합격여부, 최종결과, 코골이여부)
		//   (합격여부 Y 인 학생만 가져온다)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.납부관리, Code2.Event.REFRESH, null);
		ArrayList<Application> result = (ArrayList<Application>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 납부 여부 조회 및 관리 - UPDATE 버튼 클릭 시
	public static Tuple<Bool, String> admin_paymentManagePage_onUpdate(Application data)
	{
		//1. 클라이언트로부터 받은 학번, 생활관명, 학기로 납부여부 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 UPDATE쿼리를 쏜다.
		//	   (납부여부를 클라이언트에게서 받은 T/F로 UPDATE한다)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. UPDATE 쿼리 결과를 클라이언트에게 알려준다.
		
		Protocol protocol = eventProtocolBuilder(Code1.Page.납부관리, Code2.Event.UPDATE, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 납부 여부 조회 및 관리 - CSV 업로드 버튼 클릭 시
	public static void admin_paymentManagePage_onUpload()
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
	
	//관리자 - 서류 조회 및 제출(관리) - 들어왔을 때
	public static ArrayList<Code1.FileType> admin_documentManagePage_onEnter()
	{
		//1. 서류유형 ENUM을 배열화해서 목록을 만든다.
		//2. 배열화한 목록을 직렬화해서 클라이언트로 전송한다.
		//(3. 클라이언트는 받은 ENUM 배열을 역직렬화하여 서류유형 combobox에 표시한다)
		
		//[ENUM 배열화 예시]
		//arrayList<DocumentType> data = new arrayList<DocumentType>(DocumentType.MEDICAL, DocumentType.OATH);
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류관리, Code2.Event.REFRESH, null);
		ArrayList<Code1.FileType> result = (ArrayList<Code1.FileType>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 서류 조회 및 제출(관리) - 조회 버튼 클릭 시
	public static ArrayList<Document> admin_documentManagePage_onCheck()
	{
		//1. 서류 테이블에서 이번 학기 서류제출내역 목록을 가져와 객체화한다. (학번, 서류유형, 제출일, 진단일, 서류저장경로, 유효여부)
		//2. 배열화한다.
		//3. 직렬화해서 클라이언트에 전송한다.
		//(4. 클라이언트는 받은 배열을 tableView에 표시한다)
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류관리, Code2.Event.CHECK, null);
		ArrayList<Document> result = (ArrayList<Document>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 서류 조회 및 제출 - 삭제 버튼 클릭 시
	public static Tuple<Bool, String> admin_documentManagePage_onDelete(Document data)
	{
		//1. 클라이언트로부터 받은 학번, 서류유형, 제출일로 서류 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 DELETE 쿼리를 쏜다.
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. DELETE 쿼리 결과를 클라이언트에게 알려준다.
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류관리, Code2.Event.DELETE, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//관리자 - 서류 조회 및 제출 - 업로드 버튼 클릭 시
	public static void admin_documentManagePage_onUpload()
	{
		//학생으로부터 오프라인으로 받은 서류를 대리제출 하기 위함.
		
		//아래 로직은 student_submitDocumentPage_onSubmit와 거의 동일하다! 
		//학번만 헤더에서 따내느냐, 클라이언트에게서 입력받느냐 차이지. 그 이후로는 똑같으니 코드 재활용하시길!
		//단지 클라이언트에게 다시 알려줄때 page가 달라지거나 할듯.
		
		//1. 서버 컴퓨터 내 저장할 공간에 빈공간이 10MB보다 큰지 확인한다. -> 빈공간이 10MB보다 크면 진행, 작으면 클라이언트에게 안된다고 알려줌.
		//2. 헤더에서 파일 유형이 결핵진단서인지, 서약서인지 확인한다.
		//3. 현재 날짜로부터 학기를 특정한다. (학기는 201901 ~ 201906)
		//4. 어느폴더\학번\학기\학번+결핵진단서or서약서.jpg 와 같은 형식으로 저장된다.
		//	 (학기가 겹치면 덮어씌워진다. 즉, 한학기에 한 파일만 유효함)
		//5. 파일 저장 성공/실패 여부를 클라이언트에게 알려준다.
	}
	
	//관리자 - 서류 조회 및 제출 - UPDATE 버튼 클릭 시
	public static Tuple<Bool, String> admin_documentManagePage_onUpdate(Document data)
	{
		//1. 클라이언트로부터 받은 학번, 서류유형, 제출일, 진단일로 서류 테이블에서 조회한다.
		//2-1. 해당되는 데이터가 있으면 DB에 UPDATE쿼리를 쏜다.
		//	   (유효여부를 클라이언트에게서 받은 T/F로 UPDATE한다)
		//2-2. 해당되는 데이터가 없으면 없다고 클라이언트에 알려준다.
		//3. UPDATE 쿼리 결과를 클라이언트에게 알려준다.
		Protocol protocol = eventProtocolBuilder(Code1.Page.서류관리, Code2.Event.UPDATE, data);
		Tuple<Bool, String> result = (Tuple<Bool, String>) sendAndReceive(protocol);
		return result;
	}
	
	//-------------------------------------------------------------------------------
	//--------------------------------프로토콜 빌더----------------------------------
	//-------------------------------------------------------------------------------
	
	//이벤트 프로토콜 생성자
	private static Protocol eventProtocolBuilder(Code1.Page page, Code2.Event event, Serializable sendData)
	{
		Protocol protocol = null;
		try
		{
	        protocol = new Protocol.Builder(
	        		ProtocolType.EVENT, 
	        		Direction.TO_SERVER, 
	        		page, 
	        		event
	        		).body(ProtocolHelper.serialization(sendData)).build();
		}
		catch(Exception e)
		{
			System.out.println("[Exception.eventProtocolBuilder] Page:" + page.toString() + ", Event:" + event.toString() + 
					", DataType:" + sendData.getClass().getName());
		}
		return protocol;
	}
	
	private static Protocol fileProtocolBuilder(Code1.FileType fileType, Code2.FileCode fileCode, Serializable sendData)
	{
		Protocol protocol = null;
		try
		{
	        protocol = new Protocol.Builder(
	        		ProtocolType.FILE, 
	        		Direction.TO_SERVER, 
	        		fileType, 
	        		fileCode
	        		).body(ProtocolHelper.serialization(sendData)).build();
		}
		catch(Exception e)
		{
			System.out.println("[Exception.fileProtocolBuilder] FileType:" + fileType.toString() + ", fileCode:" + fileCode.toString() + 
					", DataType:" + sendData.getClass().getName());
		}
		return protocol;
	}
	
	//protocol을 전송하고, 결과값을 반환한다.
	private static Serializable sendAndReceive(Protocol protocol)
	{
		Serializable result = null;
        try 
        {
        	//서버에 요청하고나서 프로토콜 객체에 서버로부터 받은 정보를 담는다.
            protocol = SocketHandler.INSTANCE.request(protocol);

            //서버로부터 받은 body를 역직렬화한다. 결과로는 배열이 나올수도, 객체가 나올수도 있다. 그래서 명시적 형변환을 해주어야 한다.
            //서버에서 스케쥴 체크에 성공했을 때는 String(안내사항)과 Dormitory 배열을 전송, 실패 시 메시지와 NULL을 전송한다.
            result =  ProtocolHelper.deserialization(protocol.getBody());
        } 
        catch (Exception e) 
        {
        	System.out.println("[Exception.sendAndReceive] protocolType:" + protocol.type.toString() + ", Code1:" + protocol.code1.toString() + 
        			", Code2:" + protocol.code2.toString());
        }
        return result;
	}
	
}

//변경 로그
//2019-12-07 v1.00
//	클라이언트용 Responser.java 생성하였음 -명근

//2019-12-07 v1.01
//	관리자 페이지 생활관 조회 및 관리까지 했음. -명근
