package network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Network 패키지 내 클래스들을 총괄하는 싱글톤 클래스.
//기본적으로 네트워킹을 여기서 담당한다.
//서버 쓰레드를 만들고 관리한다.
//서버 쓰레드는 클라이언트와 연결이 되면 송신/수신 쓰레드를 만들어 배정한다.
//NetworkHandler가 서버 쓰레드를 관리하고, 서버 쓰레드는 여러개의 클라이언트 쓰레드를 관리한다.

public class NetworkHandler
{
	private static NetworkHandler _instance;
	private final int PORT = 5000;
	private ServerTask serverTask;
	private Thread serverThread;
	
	
	public static NetworkHandler getInstance()
	{
		if(_instance == null)
			_instance = new NetworkHandler();
		return _instance;
	}
	
	public void createServerThread()
	{
		try
		{
			System.out.println("networkhandler : 서버스레드 시작");
			serverTask = new ServerTask(PORT);
			serverThread = new Thread(serverTask);
			serverThread.start();
			System.out.println("networkhandler : 서버스레드 시작됨");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void prepareShutdown()
	{
		System.out.println("서버스레드 종료 시작");
		serverTask.close();
		System.out.println("서버스레드 종료됨");
	}
}
