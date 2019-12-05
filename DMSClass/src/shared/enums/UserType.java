package shared.enums;

//몇지망인지 나타내는 ENUM
public enum UserType{
	STUDENT(0, (byte)0x00),
    ADMINISTRATOR(1, (byte)0x01),
	TEACHER(2, (byte)0x02);

    public final int type;
    public final byte bit;

    UserType(int type, byte bit){
        this.type = type;
        this.bit = bit;
    }
}
