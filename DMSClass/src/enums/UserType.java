package enums;

//몇지망인지 나타내는 ENUM
public enum UserType{
	UNDEFINED(0),
    STUDENT(1),
	ADMIN(2);

    public final int type;

    UserType(int type){
        this.type = type;
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
