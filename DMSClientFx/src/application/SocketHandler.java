package application;

import ultils.Protocol;
import ultils.ProtocolHelper;

import java.io.ByteArrayOutputStream;
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

    public void write(Protocol p) throws IOException {
        for(Protocol protocol : ProtocolHelper.split(p, sendbuffer_size)){
            socket.getOutputStream().write(protocol.getPacket());
        }
    }

    public Protocol read() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[sendbuffer_size];

        while (socket.getInputStream().read(buffer) != -1) {
            output.write(buffer);
        }

        return new Protocol.Builder(output.toByteArray()).build();
    }
}
