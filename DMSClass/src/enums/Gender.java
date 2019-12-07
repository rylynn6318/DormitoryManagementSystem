package enums;

//남학생인지 여학생인지를 나타내는 ENUM
public enum Gender {
    Male("M"), Female("F");

    public final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public static Gender get(String g) {
        switch (g) {
        case "M":
            return Male;
        case "F":
            return Female;
        default:
            return null;
        }
    }
}