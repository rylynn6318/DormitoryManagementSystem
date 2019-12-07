import database.DatabaseHandler;
import enums.Code1;
import enums.Code2;
import enums.Direction;
import enums.ProtocolType;
import ultils.Protocol;
import ultils.ProtocolHelper;
import ultils.SocketHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//클라이언트와 연결을 담당하는 쓰레드
//서버소켓을 생성하고, 서버소켓이 클라이언트와 연결을 accept 하면
//해당 클라이언트에게 새 ReceiveThread와 SendThread를 만들어 준다.

public class ServerTask implements Runnable {
    private int port;
    ServerSocket serverSocket;
    DatabaseHandler db;
    Socket sock;
    private static final int THREAD_CNT = 2;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);

    private boolean stop = false;

    public ServerTask(Socket s, DatabaseHandler db) {
        sock = s;
        this.db = db;
    }

    @Override
    public void run() {
        OutputStream os = sock.getOutputStream();
        InputStream is = sock.getInputStream();
        byte protocolType;
        byte[] buffer = new byte[2000];
        //NetworkHandler에서 종료요청이 오기전까지 계속 클라이언트의 요청을 받아들인다.
        while (!stop) {
            is.read(buffer);
            Protocol protocol = null;
            try {
            	  protocol = SocketHelper.read();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            switch (protocolType) {
				case LOGIN :
					
					if(db.login(id, pw))//로그인 성공시 패킷을 클라이언트로 전송			
					{
						Protocol lp = new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT,Code1.NULL,Code2.LoginResult.STUDENT).build();
						
					}
					
					/*
						
						//로그인 실패 패킷을 클라이언트로 전송
					// login 처리*/
					break;
				case 0x02:
					//file 처리
					break;
				case 0x03:
					//event 처리
					break;
			}
        }
    }
    
    public boolean login(Stirng id, String pw) {
    	//사전처리 작업
    	boolean result = db.login(id,pw);
    	//하고싶은 작업    	
    }
   
    
    public void close() {
        System.out.println("클라이언트스레드풀 종료 시작");
        stop = true;
        try {
            //이부분에서 예외잡힘...
            //Socket socket = serverSocket.accept(); 이거랑 관련있는것 같은데, 해결법 찾아보는중...
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
        System.out.println("클라이언트스레드풀 종료됨");
    }
}
