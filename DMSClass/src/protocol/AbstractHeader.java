package protocol;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// 가장 기본적인 프로토콜 헤더
// 모든 헤더의 멤버변수는 final로 할것
abstract class AbstractHeader {
    protected final short length;
    protected final byte type;
    protected final byte direction;
    protected final byte code;
    // 각 헤더는 자신의 길이를 반환할 함수가 필요함
    // why? body 시작 인덱스를 알기 위해
    public final int header_length;

    AbstractHeader(short length, byte type, byte direction, byte code) {
        this.length = length;
        this.type = type;
        this.direction = direction;
        this.code = code;
        this.header_length = this.getHeaderLength();
    }

    // 받은 패킷으로부터 프로토콜 헤더 추출
    static AbstractHeader create(byte[] packet) {
        if (packet.length < 5)
            return null; // 먼가 짧은게 들어왔다!!

        // 아래는 공통적으로 쓰이는 헤더 부분
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(packet[0]);
        bb.put(packet[1]);
        short length = bb.getShort();
        // 자바는 byte[]의 길이를 바로 알아낼수 있어서 여기서 한번 더 체크해봄
        if (length != packet.length)
            return null; // 먼가 이상하다!
        byte type = packet[2];
        byte direction = packet[3];
        byte code = packet[4];

        return create(length, type, direction, code);
    }

    // 새로 헤더 하나 만듬
    static AbstractHeader create(short length, byte type, byte direction, byte code) {
        switch (ProtocolType.getType(type)) {
        case UNDEFINED:
            // 이 경우로 프로토콜이 생성되는 경우는 없음!
            break;
        case LOGIN:
            return new LoginProtocolHeader(length, type, direction, code);
        case FILE:
            // TODO : 구현해야함
            return new FileProtocolHeader(length, type, direction, code);
        case EVENT:
            // TODO : 구현해야함
            return new EventProtocolHeader(length, type, direction, code);
        default:
            // 이 경우로 프로토콜이 생성되면 안됨!
            break;
        }

        // 먼가 문제 생겨서 switch 내에서 return 안되면 null 리턴 == 예외다!
        // 따라서 null 체크 하셈ㅋ or 예외 던지게 코드 수정
        return null;
    }

    protected int getHeaderLength(){
        return 5;
    }

    byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(length);
        baos.write(type);
        baos.write(direction);
        baos.write(code);
        return baos.toByteArray();
    }

    short getLength() {
        return length;
    }

    ProtocolType getType() {
        return ProtocolType.getType(type);
    }

    byte getDirection() {
        return direction;
    }

    byte getCode() {
        return code;
    }
}