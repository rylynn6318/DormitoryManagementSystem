import database.DatabaseHandler;
import enums.*;
import enums.Code1.FileType;
import enums.Code1.Page;
import interfaces.ICode1;
import interfaces.ICode2;
import models.*;
import ultils.Protocol;
import ultils.ProtocolHelper;
import ultils.SocketHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//클라이언트와 연결을 담당하는 쓰레드
//서버소켓을 생성하고, 서버소켓이 클라이언트와 연결을 accept 하면
//해당 클라이언트에게 새 ReceiveThread와 SendThread를 만들어 준다.

public class ServerTask implements Runnable {
	private int port;
	ServerSocket serverSocket;
	ProtocolHelper protocolHelper;
	SocketHelper socketHelper;
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
		while (!stop) {
            Protocol protocol = null;
            try {
            	  protocol = socketHelper.read();
            } catch (Exception e) {
            	e.printStackTrace();
            }
             	ProtocolType protocolType = protocol.type;
             	
             	
            switch (protocolType) {
				case LOGIN :
					String id;
					String pw;
					try
					{
						id	  = ((Account)protocolHelper.deserialization(protocol.getBody())).accountId;
						pw	  = ((Account)protocolHelper.deserialization(protocol.getBody())).password;
		
						if(db.login(id, pw))//로그인 성공시 패킷을 클라이언트로 전송			
						{
							UserType type = ((Account)ProtocolHelper.deserialization(protocol.getBody())).userType;
							socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT,Code1.NULL,Code2.get(ProtocolType.LOGIN,type.bit)).build());
						}
						
						else
						{
							socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT,Code1.NULL,Code2.NULL).build());
						}
					}//try문 종료
					catch(Exception e)
					{
						e.printStackTrace();
					}
					break;
				case FILE:
					FileType fileCode = (FileType) protocol.code1; 
					switch(fileCode) {
					case MEDICAL_REPORT :
						
						
						break;
					case OATH:
						
						
						break;
					case CSV:
						
						
						break;
					}
					
					break;
				case EVENT:
					Page pageCode = (Page) protocol.code1; 
					switch(pageCode)
					{
						case 입사신청 :
							break;
						case 신청조회 :
							break;
						case 고지서조회 :
							break;
						case 호실조회 :
							break;
						case 서류제출 :
							break;
						case 서류조회 :
							break;
						case 선발일정관리 :
							break;
						case 생활관관리 :
							break;
						case 입사선발자관리 :
							break;
						case 입사자관리 :
							break;
						case 납부관리 :
							break;
						case 서류관리 : 
							break;
					}
					break;
			}
        }
    }

	public boolean login(String id, String pw) throws SQLException {
		// 사전처리 작업
		boolean result = db.login(id, pw);
		// 하고싶은 작업
		return result;
	}

	public void close() {
		System.out.println("클라이언트스레드풀 종료 시작");
		stop = true;
		try {
			// 이부분에서 예외잡힘...
			// Socket socket = serverSocket.accept(); 이거랑 관련있는것 같은데, 해결법 찾아보는중...
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		threadPool.shutdown();
		System.out.println("클라이언트스레드풀 종료됨");
	}
}
