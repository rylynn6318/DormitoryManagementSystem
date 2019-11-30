package protocol;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// 가장 기본적인 프로토콜 헤더
// Abstract으로 쓰고 싶었지만 AbstractProtocol에 멤버 변수로 들어가는 관계로 그냥 abstract 키워드 못씀
public class BaseHeader {
    private short length;
    private byte type;
    private byte response_direction;
    private byte code;
    
    protected BaseHeader(){

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



        return null;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(length);
        baos.write(type);
        baos.write(response_direction);
        baos.write(code);
        return baos.toByteArray();
    }
}