package application;

import ultils.Protocol;

import java.io.IOException;
import java.net.Socket;

public enum SocketHandler {
    INSTANCE;

    public final String host = "127.0.0.1";
    public final int port = 4444;
    public final int sendbuffer_size = 1024;

    private Socket socket = null;

    SocketHandler(){
        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            IOHandler.getInstance().showAlert("네트워크 예외! 소켓 생성 실패!");
            e.printStackTrace();
        }
    }

    public void send(Protocol p) throws IOException {
        socket.getOutputStream().write(p.getPacket());
    }

    public byte[] read() throws IOException {
        byte[] buffer = new byte[sendbuffer_size];
        socket.getInputStream().read(buffer);
        return buffer;
    }
}
