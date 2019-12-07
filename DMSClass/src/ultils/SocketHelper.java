package ultils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import enums.Bool;

// 프로토콜을 이용한 소켓 통신을 쉽게 해주는 wrapper 클래스
public class SocketHelper {
    public final static String host = "127.0.0.1";
    public final static int port = 4444;
    public final static int sendbuffer_size = 1024;

    private Socket socket = null;

    public SocketHelper(Socket socket) {
        this.socket = socket;
    }

    public void write(Protocol p) throws IOException {
        List<Protocol> protocols = ProtocolHelper.split(p, sendbuffer_size);
        for (Protocol protocol : protocols) {
            byte[] packet = protocol.getPacket();
            socket.getOutputStream().write(packet);
        }
    }

    public Protocol read() throws Exception {
        List<Protocol> protocols = new ArrayList<>();
        byte[] buffer = new byte[sendbuffer_size];

        Bool isLast = Bool.FALSE;
        while (!isLast.bool) {
            socket.getInputStream().read(buffer);
            Protocol tmp = new Protocol.Builder(buffer).build();
            protocols.add(tmp);
            isLast = tmp.is_last;
        }

        return ProtocolHelper.merge(protocols);
    }
}
