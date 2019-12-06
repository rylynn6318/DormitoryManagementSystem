package enums;

//남학생인지 여학생인지를 나타내는 ENUM
public enum Gender {
    Male('M', (byte) 0x00), Female('F', (byte) 0x01);

    public final char gender;
    public final byte bit;

    Gender(char gender, byte bit) {
        this.gender = gender;
        this.bit = bit;
    }

    public static Gender get(char g) {
        if (g == 'M')
            return Male;
        else
            return Female;
    }
}