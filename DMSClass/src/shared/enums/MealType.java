package shared.enums;

//식사안함, 5일식, 7일식을 나타내는 ENUM
public enum MealType{
    NOMEAL(0, (byte)0x00),
    FIVEDAY(5, (byte)0x01),
	SEVENDAY(7, (byte)0x02);

    public final int meal;
    public final byte bit;

    MealType(int meal, byte bit){
        this.meal = meal;
        this.bit = bit;
    }
}