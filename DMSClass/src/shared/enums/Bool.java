package shared.enums;

public enum Bool{
    TRUE(true, "Y", (byte)0x01),
    FALSE(false, "N", (byte)0x00);

    public final boolean bool;
    public final String yn;
    public final byte bit;

    Bool(boolean bool, String yn, byte bit){
        this.bool = bool;
        this.yn = yn;
        this.bit = bit;
    }
}