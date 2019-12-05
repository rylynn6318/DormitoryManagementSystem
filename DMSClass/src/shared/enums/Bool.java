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

    public static Bool get(boolean obj){
        if(Bool.TRUE.bool == obj)
            return Bool.TRUE;
        else
            return Bool.FALSE;
    }
    public static Bool get(String obj){
        if(Bool.TRUE.yn.equals(obj))
            return Bool.TRUE;
        else
            return Bool.FALSE;
    }
    public static Bool get(byte obj){
        if(Bool.TRUE.bit == obj)
            return Bool.TRUE;
        else
            return Bool.FALSE;
    }
}