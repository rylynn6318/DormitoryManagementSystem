package enums;

// 프로토콜 만들때 필수 파라메터인
// 타입, 방향, 코드타입, 코드에 대한 Enum 클래스
// TODO : code1, code2 네이밍 아무리봐도 수정이 필요해 보임
public final class ProtocolField {
    protected interface IGetCode {
        public byte getCode();
    }

    // 프로토콜 타입
    public static enum Type implements IGetCode {
        LOGIN((byte) 0x01), FILE((byte) 0x02), EVENT((byte) 0x03);

        private final byte code;

        @Override
        public byte getCode() {
            return code;
        }

        Type(byte code) {
            this.code = code;
        }

        public static Type get(byte code) {
            switch (code) {
            case 0x01:
                return LOGIN;
            case 0x02:
                return FILE;
            case 0x03:
                return EVENT;
            }
            return null;
        }
    }

    // 프로토콜 방향
    public static enum Direction implements IGetCode {
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

    // 코드1
    public static class Code1 {
        public interface ICode1 extends IGetCode {
        }

        public static ICode1 get(Type type, byte code) {
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
            MEDICAL_REPORT((byte) 0x01), OATH((byte) 0x02), CSV((byte) 0x03);

            private final byte code;

            @Override
            public byte getCode() {
                return code;
            }

            FileType(byte code) {
                this.code = code;
            }

            public static FileType get(byte code) {
                switch (code) {
                case 0x01:
                    return MEDICAL_REPORT;
                case 0x02:
                    return OATH;
                case 0x03:
                    return CSV;
                }
                return null;
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
                }
                return null;
            }
        }
    }

    // 코드2
    public static class Code2 {
        public interface ICode2 extends IGetCode {
        }

        public static ICode2 get(Type type, byte code) {
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
}