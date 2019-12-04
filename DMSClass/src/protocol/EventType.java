package protocol;

public enum EventType{
	
	REFRESH, CHECK, SUBMIT, CANCEL, UPDATE, DELETE, SELECTION, ASSIGN;
	
	public static EventType getDirection(int x) {
		switch (x) {
		case 0:
			//페이지 진입 시 호출, 일정 로드 및 해당 페이지 갱신 시 필요한 정보를 불러옴.
			return REFRESH;
		case 1:
			//조회 버튼 클릭 시 발생
			return CHECK;
		case 2:
			//등록 버튼 클릭 시 발생
			return SUBMIT;
		case 3:
			//취소 버튼 클릭 시 발생
			return CANCEL;
		case 4:
			//Update 버튼 클릭 시 발생
			return UPDATE;
		case 5:
			//삭제 버튼 클릭 시 발생
			return DELETE;
		case 6:
			//입사자 선발
			return SELECTION;
		case 7:
			//입사자 등록 (방배정)
			return ASSIGN;
		}
		
		return null;
	}

}
