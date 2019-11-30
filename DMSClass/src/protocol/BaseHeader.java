package protocol;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// 가장 기본적인 프로토콜 헤더
// Abstract으로 쓰고 싶었지만 AbstractProtocol에 멤버 변수로 들어가는 관계로 그냥 abstract 키워드 못씀
// 근데 어짜피 접근제한자로 외부 패키지에선 접근 못함
// 모든 헤더의 멤버변수는 final로 할것
class BaseHeader {
    private final short length;
    private final byte type;
    private final byte direction;
    private final byte code;

    public static class Builder<T extends Builder<T>>{
        private short length;
        private byte type;
        private byte direction;
        private byte code;
        
        public Builder(){}

        public T length(short length){
            this.length = length;
            return (T)this;
        }
        public T type(ProtocolType type){
            this.type = (byte)type.ordinal();
            return (T)this;
        }
        public T direction(byte direction){
            this.direction = direction;
            return (T)this;
        }
        public T code(byte code){
            this.code = code;
            return (T)this;
        }
        public BaseHeader build(){
            return new BaseHeader(this);
        }
    }

    protected BaseHeader(Builder<?> builder){
        this.length = builder.length;
        this.type = builder.type;
        this.direction = builder.direction;
        this.code = builder.code;
    }

    public static BaseHeader create(byte[] packet){
        if(packet.length < 5)
            return null;

        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(packet[0]);
        bb.put(packet[1]);
        short length = bb.getShort();
        // 자바는 byte[]의 길이를 바로 알아낼수 있어서 여기서 한번 더 체크해봄
        if(length != packet.length)
            return null;
        byte type = packet[2];
        byte response_direction = packet[3];
        byte code = packet[4];

        switch(ProtocolType.getType(packet[2])){
            case UNDEFINED:
                // 이 경우로 프로토콜이 생성되는 경우는 없음!
                break;
            case LOGIN:
                return new LoginProtocolHeader.Builder().build();
            case FILE:
                return new FileProtocolHeader.Builder().build();
            case EVENT:
                return new EventProtocolHeader.Builder().build();
            default:
                // 이 경우로 프로토콜이 생성되면 안됨!
                break;
        }

        // 먼가 문제 생겨서 switch 내에서 return 안되면 null 리턴 == 예외 발생!
        return null;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(length);
        baos.write(type);
        baos.write(direction);
        baos.write(code);
        return baos.toByteArray();
    }

    public short getLength() {
        return length;
    }
    public ProtocolType getType() {
        return ProtocolType.getType(type);
    }
    public byte getDirection() {
        return direction;
    }
    public byte getCode() {
        return code;
    }
}