package ultils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

// 프로토콜을 이용한 소켓 통신을 쉽게 해주는 wrapper 클래스
public class SocketHelper {
    public final static String host = "127.0.0.1";
    public final static int port = 4444;
    public final static int sendbuffer_size = 1024;

    private Socket socket = null;

    public SocketHelper(Socket socket){
        this.socket = socket;
    }

    public void write(Protocol p) throws IOException {
        for(Protocol protocol : ProtocolHelper.split(p, sendbuffer_size)){
            socket.getOutputStream().write(protocol.getPacket());
        }
    }

    public Protocol read() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[sendbuffer_size];

        int readed = 0;
        while ((readed = socket.getInputStream().read(buffer)) != -1) {
            output.write(buffer, 0, readed);
        }

        return new Protocol.Builder(output.toByteArray()).build();
    }
}
