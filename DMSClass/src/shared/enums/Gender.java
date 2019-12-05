package shared.enums;

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