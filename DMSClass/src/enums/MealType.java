package enums;

//식사안함, 5일식, 7일식을 나타내는 ENUM
public enum MealType{
    NOMEAL(0),
    FIVEDAY(5),
	SEVENDAY(7);

    public final int meal;

    MealType(int meal){
        this.meal = meal;
    }
}