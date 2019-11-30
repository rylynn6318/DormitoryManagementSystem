package protocol;

public enum ProtocolType{
    UNDEFINED, LOGIN, FILE, EVENT;

	public static ProtocolType getType(int x){
		switch(x){
			case 0:
				return UNDEFINED;
			case 1:
				return LOGIN;
			case 2:
				return FILE;
			case 3:
				return EVENT;
		}
		return null;
	}
}