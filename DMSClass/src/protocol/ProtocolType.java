package protocol;

public enum ProtocolType{
    UNDEFINED, EXIT, LOGIN_TRY, LOGIN_RESULT, FILE_UPLOAD, FILE_DOWNLOAD;

	public static ProtocolType getType(int x){
		switch(x){
			case 0:
				return UNDEFINED;
			case 1:
				return EXIT;
			case 2:
				return LOGIN_REQUEST_ID;
			case 3:
				return LOGIN_REQUEST_PW;
			case 4:
				return LOGIN_RESPONSE_ID;
			case 5:
				return LOGIN_RESPONSE_PW;
			case 6:
				return LOGIN_RESULT;
		}
		return null;
	}
}