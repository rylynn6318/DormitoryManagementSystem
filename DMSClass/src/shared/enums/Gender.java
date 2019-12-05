package shared.enums;

//남학생인지 여학생인지를 나타내는 ENUM
public enum Gender{
    M("M", (byte)0x00),
    F("F", (byte)0x01);

    public final String gender;
    public final byte bit;

    Gender(String gender, byte bit){
        this.gender = gender;
        this.bit = bit;
    }
}