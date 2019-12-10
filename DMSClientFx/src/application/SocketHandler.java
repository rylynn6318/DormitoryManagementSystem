package application;

import utils.*;

import java.net.Socket;

// 클라이언트에서는 이 싱글톤을 통해서만 통신하고
// 여기선 소켓 생성부터 결과값까지 하나의 함수로 처리함.
public enum SocketHandler {
	
    INSTANCE;
	
	public String ip = SocketHelper.localhost;

    public Protocol request(Protocol protocol) throws Exception {
        Socket socket = new Socket(ip,SocketHelper.port);
        SocketHelper socketHelper = new SocketHelper(socket);

        socketHelper.write(protocol);
        Protocol result = socketHelper.read();

        socket.close();
        return result;
    }
}
