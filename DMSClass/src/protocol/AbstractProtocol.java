package protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class AbstractProtocol {
    private BaseHeader head;
    private byte[] body;

    // 새 프로토콜 만들고 head 할당함.
    // 생성자는 쓰지말고 create로 만드셈.
    protected AbstractProtocol() {
    }

    // 패킷으로부터 프로토콜을 만듬.
    // @Builder까진 필요없을듯
    public static AbstractProtocol create(byte[] packet) {

        return null;
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    public byte[] getBytes() {
        byte[] headbyte = head.getBytes();
        byte[] packet = new byte[headbyte.length + body.length];
        System.arraycopy(headbyte, 0, packet, 0, headbyte.length);
        System.arraycopy(body, 0, packet, headbyte.length, body.length);
        return packet;
    }

    // Serializable 객체를 직렬화 해서 body에 넣음.
    public void setBody(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
                body = baos.toByteArray();
            }
        }
    }

    // body에 있는 바이트 배열을 역직렬화해서 Serializable 객체 반환. 이후 캐스팅은 알아서 하셈.
    public Serializable getBody() throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object obj = ois.readObject();
                return (Serializable) obj;
            }
        }
    }
}