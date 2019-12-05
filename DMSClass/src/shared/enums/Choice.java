package shared.enums;

//몇지망인지 나타내는 ENUM
public enum Choice{
	ONEYEAR(0, (byte)0x00),
    FIRST(1, (byte)0x01),
	SECOND(2, (byte)0x02),
	THIRD(3, (byte)0x03);

    public final int choice;
    public final byte bit;

    Choice(int choice, byte bit){
        this.choice = choice;
        this.bit = bit;
    }
}
