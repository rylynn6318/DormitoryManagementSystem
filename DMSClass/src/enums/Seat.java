package enums;

public enum Seat {
    FIRST(0, (byte) 0x00, 'A'), SECOND(1, (byte) 0x01, 'B'), THIRD(2, (byte) 0x02, 'C'), FOURTH(3, (byte) 0x03, 'D');

    public final int seatNum;
    public final byte bit;
    public final char abcd;

    Seat(int seatNum, byte bit, char abcd) {
        this.seatNum = seatNum;
        this.bit = bit;
        this.abcd = abcd;
    }

    public static Seat get(char abcd) {
        switch (abcd) {
        case 'A':
            return FIRST;
        case 'B':
            return SECOND;
        case 'C':
            return THIRD;
        case 'D':
            return FOURTH;
        default:
            return null;
        }
    }
}
