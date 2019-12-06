package enums;

import interfaces.*;

public enum ProtocolType implements IGetCode {
    LOGIN((byte) 0x01), FILE((byte) 0x02), EVENT((byte) 0x03);

    private final byte code;

    @Override
    public byte getCode() {
        return code;
    }

    ProtocolType(byte code) {
        this.code = code;
    }

    public static ProtocolType get(byte code) {
        switch (code) {
        case 0x01:
            return LOGIN;
        case 0x02:
            return FILE;
        case 0x03:
            return EVENT;
        default:
            return null;
        }
    }
}
