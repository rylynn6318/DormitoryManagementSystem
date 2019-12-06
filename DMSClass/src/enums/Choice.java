package enums;

//몇지망인지 나타내는 ENUM
public enum Choice {
    ONEYEAR(0, (byte) 0x00), FIRST(1, (byte) 0x01), SECOND(2, (byte) 0x02), THIRD(3, (byte) 0x03);

    public final int choice;
    public final byte bit;

    Choice(int choice, byte bit) {
        this.choice = choice;
        this.bit = bit;
    }

    public static Choice get(int choice) {
        switch (choice) {
        case 0:
            return ONEYEAR;
        case 1:
            return FIRST;
        case 2:
            return SECOND;
        case 3:
            return THIRD;
        default:
            return null;
        }
    }
}
