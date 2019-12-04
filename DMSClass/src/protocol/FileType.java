package protocol;

// 지금은 안쓰고 있음.
// 혹시 추후에 프로토콜 byte로 쓰다가 헷갈릴수도 있으니 일단 준비함.
// 추가 : 이거 안쓰고 바이트로 쓰려니 헷갈려서 결국 사용하기로 결정함.
public enum FileType{
	
	MEDICAL_REPORT, OATH, CSV;
	
	public static FileType getDirection(int x) {
		switch (x) {
		case 0:
			return MEDICAL_REPORT;
		case 1:
			return OATH;
		case 2:
			return CSV;
		}
		return null;
	}

}