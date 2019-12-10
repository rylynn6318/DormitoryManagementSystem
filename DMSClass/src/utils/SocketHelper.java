package utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.Bool;

// 프로토콜을 이용한 소켓 통신을 쉽게 해주는 wrapper 클래스
public final class SocketHelper implements Closeable {
    public final static String localhost = "127.0.0.1";
    public final static int port = 4444;
    public final static int send_buffer_size = 1024*1024*10;
    public final static int timeout = 15000; // 15초, 아직 안씀

    private Socket socket = null;

    public SocketHelper(Socket socket) {
        this.socket = socket;
    }

    public void write(Protocol p) {
        //List<Protocol> protocols = ProtocolHelper.split(p);
        //for (Protocol protocol : protocols) {
            byte[] packet = p.getPacket();
            try {
                socket.getOutputStream().write(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
    }

    public Protocol read() {
        List<Protocol> protocols = new ArrayList<>();
        byte[] buffer = new byte[send_buffer_size];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int bytesRead = socket.getInputStream().read(buffer, 0, buffer.length);
            int packet_length = ProtocolHelper.bytesToInt(Arrays.copyOfRange(buffer, 0, 4));
            int current = bytesRead;
            //baos.write(buffer);

            while (current < packet_length) {
                bytesRead = socket.getInputStream().read(buffer,current,packet_length - current);
                if (bytesRead >= 0) {
                    //baos.write(buffer);
                    current += bytesRead;
                }
            }

            buffer = Arrays.copyOfRange(buffer,0,current);
        }catch (Exception e) {
            System.out.println("----프로토콜 read 중 오류 발생 여기부터----");
            e.printStackTrace();
            System.out.println("----프로토콜 read 중 오류 발생 여기까지----");
        }

        return new Protocol.Builder(buffer).build();
    }

    public InetAddress getInetAddress(){
        return socket.getInetAddress();
    }


    @Override
    public void close() throws IOException {
        this.socket.close();
    }
}
