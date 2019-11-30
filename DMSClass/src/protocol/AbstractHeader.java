package protocol;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// 가장 기본적인 프로토콜 헤더
// 헤더 자체가 바디가 결정되면 code 정도를 제외하곤 리플렉션 등을 이용해 전부 값을 자동으로 할당해 줄 수 있음으로 헤더 클래스 자체를 외부에 노출시킬 것인지 고민해 봐야할듯?
// 모든 헤더의 멤버변수는 final로 할것 why? 프로토콜에서 body와 code를 받는 시점에서 나머지 정보는 전부 자동으로 알 수 있음.
abstract class AbstractHeader {
    private final short length;
    private final byte type;
    private final byte direction;
    private final byte code;

    AbstractHeader(short length, byte type, byte direction, byte code) {
        this.length = length;
        this.type = type;
        this.direction = direction;
        this.code = code;
    }

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

        switch (ProtocolType.getType(packet[2])) {
        case UNDEFINED:
            // 이 경우로 프로토콜이 생성되는 경우는 없음!
            break;
        case LOGIN:
            return new LoginProtocolHeader(length, type, direction, code);
        case FILE:
            return new FileProtocolHeader(length, type, direction, code);
        case EVENT:
            return new EventProtocolHeader(length, type, direction, code);
        default:
            // 이 경우로 프로토콜이 생성되면 안됨!
            break;
        }

        // 먼가 문제 생겨서 switch 내에서 return 안되면 null 리턴 == 예외다! 따라서 null 체크 하셈ㅋ or 예외 던지게 코드
        // 수정
        return null;
    }

    // 각 헤더는 자신의 길이를 반환할 함수를 구현해야함
    // why? body 시작 인덱스를 알기 위해
    // 근데 그렇다고 함수 이름을 getBodyStartIndex()로 하는거 보단 이게 좋은듯
    // 또한 final 변수로 쓰는거보단 이게 나을듯
    abstract int getHeaderLength();

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