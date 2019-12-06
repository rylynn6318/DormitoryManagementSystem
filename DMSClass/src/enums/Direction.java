package enums;

import interfaces.*;

// 프로토콜 방향
public enum Direction implements IGetCode {
    TO_SERVER((byte) 0x00), TO_CLIENT((byte) 0x01);

    private final byte code;

    @Override
    public byte getCode() {
        return code;
    }

    Direction(byte code) {
        this.code = code;
    }

    public static Direction get(byte code) {
        switch (code) {
        case 0x00:
            return TO_SERVER;
        case 0x01:
            return TO_CLIENT;
        }
        return null;
    }
}