package ultils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// singleton
public enum SocketHelper {
    CLIENT(), SERVER(false);

    public final static String host = "127.0.0.1";
    public final static int port = 4444;
    public final static int sendbuffer_size = 1024;

    private boolean isServerSocket;
    private Socket socket;

    private SocketHelper() {
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.isServerSocket = false;
    }

    private SocketHelper(boolean isInit){
        // for serverSocket.accept()
        this.isServerSocket = true;
    }
}