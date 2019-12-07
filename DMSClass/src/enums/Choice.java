package enums;

//몇지망인지 나타내는 ENUM
public enum Choice {
    ONEYEAR(0), FIRST(1), SECOND(2), THIRD(3);

    public final int choice;

    Choice(int choice) {
        this.choice = choice;
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
