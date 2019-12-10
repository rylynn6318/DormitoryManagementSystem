package utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import enums.Bool;

// 프로토콜을 이용한 소켓 통신을 쉽게 해주는 wrapper 클래스
public final class SocketHelper implements Closeable {
    public final static String localhost = "127.0.0.1";
    public final static int port = 4444;
    public final static int sendbuffer_size = 8192;
    public final static int timeout = 15000; // 15초, 아직 안씀

    private Socket socket = null;

    public SocketHelper(Socket socket) {
        this.socket = socket;
    }

    public void write(Protocol p) {
        List<Protocol> protocols = ProtocolHelper.split(p, sendbuffer_size);
        for (Protocol protocol : protocols) {
            byte[] packet = protocol.getPacket();
            try {
                socket.getOutputStream().write(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Protocol read() {
        List<Protocol> protocols = new ArrayList<>();
        byte[] buffer = new byte[sendbuffer_size];

        Bool isLast = Bool.FALSE;
        while (!isLast.bool) {
            try {
                socket.getInputStream().read(buffer);
                Protocol tmp = new Protocol.Builder(buffer).build();
                protocols.add(tmp);
                isLast = tmp.is_last;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ProtocolHelper.merge(protocols);
    }

    public InetAddress getInetAddress(){
        return socket.getInetAddress();
    }


    @Override
    public void close() throws IOException {
        this.socket.close();
    }
}
