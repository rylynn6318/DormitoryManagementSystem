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
				return LOGIN_TRY;
			case 3:
				return LOGIN_RESULT;
			case 4:
				return FILE_UPLOAD;
			case 5:
				return FILE_DOWNLOAD;
		}
		return null;
	}
}