package protocol;

import java.io.ByteArrayOutputStream;

public class ProtocolHeader {
    private short length;
    private byte type;
    private byte response_direction;
    private byte code;

    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(length);
        baos.write(type);
        baos.write(response_direction);
        baos.write(code);
        return baos.toByteArray();
    }
}