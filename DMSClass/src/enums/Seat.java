package enums;

public enum Seat{
	FIRST(0, (byte)0x00),
	SECOND(1, (byte)0x01),
	THIRD(2, (byte)0x02),
	FOURTH(3, (byte)0x03);

    public final int seatNum;
    public final byte bit;

    Seat(int seatNum, byte bit){
        this.seatNum = seatNum;
        this.bit = bit;
    }
}
