package protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class AbstractProtocol {
    private ProtocolHeader head;
    private byte[] packet;

    protected AbstractProtocol(byte[] packet) {
        this.packet = packet;
    }

    public void makeProtocol(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
                // serializedMember -> 직렬화된 member 객체 
                packet = baos.toByteArray();
            }
        }
    }
}