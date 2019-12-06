package ultils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import enums.*;
import interfaces.*;

// 사용법
// 1. Builder를 통해 프로토콜을 만든다.
// 2. SocketHelper를 통해 통신한다
// 3. profit!

public final class Protocol implements Comparable<Protocol> {
    // 프로토콜 생성시 Builder 이용할 것
    public static class Builder {
        // 필수
        private final ProtocolType type; // 1바이트, 프로토콜 타입
        private final Direction direction; // 1바이트, 프로토콜 응답 방향
        private final ICode1 code1; // 1바이트, 프로토콜 코드
        private final ICode2 code2; // 1바이트, 프로토콜 코드
        // 옵션
        private short length = HEADER_LENGTH; // 2바이트, 전체 프로토콜 길이.
                                              // 실제 프로토콜에선 필수 정보지만 헤더길이는 10으로 고정되어 있으니
                                              // body 받고나면 길이 계산 가능해서 Builder에선 옵션임.
        private Bool is_splitted = Bool.FALSE; // 1바이트, 프로토콜 분리 여부
        private Bool is_last = Bool.FALSE; // 1바이트, 마지막 프로토콜인지 여부
        private short sequence = 0; // 2바이트, 시퀀스 넘버
        private byte[] body_bytes = null; // body가 직렬화 된것

        public Builder(ProtocolType type, Direction direction, ICode1 code1, ICode2 code2) {
            this.type = type;
            this.direction = direction;
            this.code1 = code1;
            this.code2 = code2;
        }

        public Builder sequence(short seq, Bool islast) {
            is_splitted = Bool.TRUE;
            sequence = seq;
            is_last = islast;
            return this;
        }

        public Builder body(byte[] serialized){
            body_bytes = serialized;
            this.length = (short) (HEADER_LENGTH + serialized.length);
            return this;
        }

        public Builder(byte[] packet) throws Exception {
            if (packet.length < HEADER_LENGTH)
                throw new Exception("패킷 길이가 먼가 짧다!!!");

            // 빅 엔디안으로 읽음
            this.length = ProtocolHelper.bytesToShort(packet[0], packet[1]); // (short) (packet[0] << 8 | (packet[1] & 0xFF));
            this.type = ProtocolType.get(packet[2]);
            this.direction = Direction.get(packet[3]);
            this.code1 = Code1.get(this.type, packet[4]);
            this.code2 = Code2.get(this.type, packet[5]);
            this.is_splitted = Bool.get(packet[6]);
            this.is_last = Bool.get(packet[7]);
            this.sequence = ProtocolHelper.bytesToShort(packet[8], packet[9]); //(short) (packet[8] << 8 | (packet[9] & 0xFF));

            this.body_bytes = Arrays.copyOfRange(packet, HEADER_LENGTH, packet.length);
        }

        // body에 아무것도 할당하지 않았을 경우 null을 직렬화해서 body에 할당함
        // why? 진짜 아무것도 없는거보단 null이 들어가게 만들기 위해
        public Protocol build() throws IOException {
            if (body_bytes == null)
                body(ProtocolHelper.serialization(null));

            return new Protocol(this);
        }
    }

    // Header
    public final short length; // 2바이트, 전체 프로토콜 길이
    public final ProtocolType type; // 1바이트, 프로토콜 타입
    public final Direction direction; // 1바이트, 프로토콜 응답 방향
    public final ICode1 code1; // 1바이트, 프로토콜 코드 종류
    public final ICode2 code2; // 1바이트, 프로토콜 코드
    // 아래 3개는 body가 커져서 프로토콜 분리시 쓰임
    public final Bool is_splitted;// 1바이트, 프로토콜 분리 여부
    public final Bool is_last; // 1바이트, 마지막 프로토콜인지 여부
    public final short sequence; // 2바이트, 시퀀스 넘버
    // body 시작 인덱스를 알기 위해 자신의 길이를 가지고 있다.
    public static final int HEADER_LENGTH = 10;

    // Body
    private final byte[] body_bytes;

    public byte[] getBody() {
        return body_bytes;
    }

    // Builder로부터 프로토콜 생성
    protected Protocol(Builder builder) throws IOException {
        this.length = builder.length;
        this.type = builder.type;
        this.direction = builder.direction;
        this.code1 = builder.code1;
        this.code2 = builder.code2;
        this.is_splitted = builder.is_splitted;
        this.is_last = builder.is_last;
        this.sequence = builder.sequence;
        this.body_bytes = builder.body_bytes;
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    // 순서는 변수 선언 순서와 같음
    public byte[] getPacket() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // short 타입은 Big Endian으로 변환
        baos.write(ProtocolHelper.shortToByte(length)[0]); // (byte) ((length >> 8) & 0xff)
        baos.write(ProtocolHelper.shortToByte(length)[1]); // (byte) (length & 0xff)
        baos.write(type.getCode());
        baos.write(direction.getCode());
        baos.write(code1.getCode());
        baos.write(code2.getCode());
        baos.write(is_splitted.bit);
        baos.write(is_last.bit);
        baos.write(ProtocolHelper.shortToByte(length)[0]); // (byte) ((sequence >> 8) & 0xff)
        baos.write(ProtocolHelper.shortToByte(length)[1]); // (byte) (sequence & 0xff)
        baos.write(body_bytes);
        return baos.toByteArray();
    }

    @Override
    public int compareTo(Protocol o) {
        if (this.sequence < o.sequence)
            return -1;
        else if (this.sequence > o.sequence)
            return 1;
        else if (this.sequence == 0 && o.sequence == 0)
            return 0;
        else
            throw new RuntimeException("두 프로토콜의 시퀀스가 같으면 안됩니다!");
    }
}