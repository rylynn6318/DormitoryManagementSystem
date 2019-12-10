package enums;

import interfaces.ICode1;

public class Code1 {
    public static ICode1 get(ProtocolType type, byte code) {
        switch (type) {
        case LOGIN:
            return Null.NULL;
        case FILE:
            return FileType.get(code);
        case EVENT:
            return Page.get(code);
        default:
            // 이건 예외 상황이다!
            return null;
        }
    }

    public final static Null NULL = Code1.Null.NULL;

    protected static enum Null implements ICode1 {
        NULL((byte) 0x00);

        private final byte code;

        @Override
        public byte getCode() {
            return code;
        }

        Null(byte code) {
            this.code = code;
        }
    }

    public static enum FileType implements ICode1 {
        MEDICAL_REPORT((byte) 0x01, ".jpg"), OATH((byte) 0x02, ".jpg"), CSV((byte) 0x03, ".csv");

        private final byte code;
        public final String extension;

        @Override
        public byte getCode() {
            return code;
        }

        FileType(byte code, String extension) {
            this.code = code;
            this.extension = extension;
        }

        public static FileType get(byte code) {
            switch (code) {
            case 0x01:
                return MEDICAL_REPORT;
            case 0x02:
                return OATH;
            case 0x03:
                return CSV;
            default:
                return null;
            }
        }
    }

    public static enum Page implements ICode1 {
        입사신청((byte) 0x01), 신청조회((byte) 0x02), 고지서조회((byte) 0x03), 호실조회((byte) 0x04), 서류제출((byte) 0x05),
        서류조회((byte) 0x06), 선발일정관리((byte) 0x07), 생활관관리((byte) 0x08), 입사선발자관리((byte) 0x09), 입사자관리((byte) 0x0A),
        납부관리((byte) 0x0B), 서류관리((byte) 0x0C);

        private final byte code;

        @Override
        public byte getCode() {
            return code;
        }

        Page(byte code) {
            this.code = code;
        }

        public static Page get(byte code) {
            switch (code) {
            case 0x01:
                return 입사신청;
            case 0x02:
                return 신청조회;
            case 0x03:
                return 고지서조회;
            case 0x04:
                return 호실조회;
            case 0x05:
                return 서류제출;
            case 0x06:
                return 서류조회;
            case 0x07:
                return 선발일정관리;
            case 0x08:
                return 생활관관리;
            case 0x09:
                return 입사선발자관리;
            case 0x0A:
                return 입사자관리;
            case 0x0B:
                return 납부관리;
            case 0x0C:
                return 서류관리;
            default:
                return null;
            }
        }
    }
}