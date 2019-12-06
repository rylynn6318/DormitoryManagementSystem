package enums;

//몇지망인지 나타내는 ENUM
public enum UserType{
	UNDEFINED(0, (byte)0x00),
    STUDENT(1, (byte)0x01),
	ADMIN(2, (byte)0x02);

    public final int type;
    public final byte bit;

    UserType(int type, byte bit){
        this.type = type;
        this.bit = bit;
    }

    public static UserType get(Code2.LoginResult result){
        switch(result){
            case FAIL:
                return UNDEFINED;
            case STUDENT:
                return STUDENT;
            case ADMIN:
                return ADMIN;
            default:
                return UNDEFINED;
        }
    }
}
