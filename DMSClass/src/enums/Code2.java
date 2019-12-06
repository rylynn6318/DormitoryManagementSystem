package enums;

import interfaces.ICode2;

public class Code2 {
    public static ICode2 get(ProtocolType type, byte code) {
        switch (type) {
        case LOGIN:
            return Code2.LoginResult.get(code);
        case FILE:
            return Code2.FileCode.get(code);
        case EVENT:
            return Code2.Event.get(code);
        default:
            // 예외 상황!
            return null;
        }
    }

    public final static Null NULL = Code2.Null.NULL;

    protected static enum Null implements ICode2 {
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

    public static enum LoginResult implements ICode2 {
        FAIL((byte) 0x00), STUDENT((byte) 0x01), ADMIN((byte) 0x02);

        private final byte code;

        @Override
        public byte getCode() {
            return code;
        }

        LoginResult(byte code) {
            this.code = code;
        }

        public static LoginResult get(byte code) {
            switch (code) {
            case 0x00:
                return FAIL;
            case 0x01:
                return STUDENT;
            case 0x02:
                return ADMIN;
            }

            return null;
        }
    }

    public static enum FileCode implements ICode2 {
        UPLOAD((byte) 0x01), REQUEST_DOWNLOAD((byte) 0x02), UPLOAD_RESULT((byte) 0x03), DOWNLOAD((byte) 0x04);

        private final byte code;

        @Override
        public byte getCode() {
            return code;
        }

        FileCode(byte code) {
            this.code = code;
        }

        public static FileCode get(byte code) {
            switch (code) {
            case 0x01:
                return UPLOAD;
            case 0x02:
                return REQUEST_DOWNLOAD;
            case 0x03:
                return UPLOAD_RESULT;
            case 0x04:
                return DOWNLOAD;
            }

            return null;
        }
    }

    public static enum Event implements ICode2 {
        REFRESH((byte) 0x01), CHECK((byte) 0x02), SUBMIT((byte) 0x03), CANCEL((byte) 0x04), UPDATE((byte) 0x05),
        DELETE((byte) 0x06), SELECTION((byte) 0x07), ASSIGN((byte) 0x08);

        private final byte code;

        @Override
        public byte getCode() {
            return code;
        }

        Event(byte code) {
            this.code = code;
        }

        public static Event get(byte code) {
            switch (code) {
            case 0x01:
                // 페이지 진입 시 호출, 일정 로드 및 해당 페이지 갱신 시 필요한 정보를 불러옴.
                return REFRESH;
            case 0x02:
                // 조회 버튼 클릭 시 발생
                return CHECK;
            case 0x03:
                // 등록 버튼 클릭 시 발생
                return SUBMIT;
            case 0x04:
                // 취소 버튼 클릭 시 발생
                return CANCEL;
            case 0x05:
                // Update 버튼 클릭 시 발생
                return UPDATE;
            case 0x06:
                // 삭제 버튼 클릭 시 발생
                return DELETE;
            case 0x07:
                // 입사자 선발
                return SELECTION;
            case 0x08:
                // 입사자 등록 (방배정)
                return ASSIGN;
            }

            return null;
        }
    }
}