package ultils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ProtocolHelper {
    public static byte[] serialization(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
                return baos.toByteArray();
            }
        }
    }

    public static Serializable deserialization(byte[] bytes) throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object obj = ois.readObject();
                return (Serializable) obj;
            }
        }
    }
}