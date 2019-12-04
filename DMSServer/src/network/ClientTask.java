package network;

import java.net.Socket;

//클라이언트와 통신 중 수신을 담당하는 쓰레드

public class ClientTask implements Runnable
{
	Socket socket;
	public ClientTask(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run()
	{
		//커넥션 맺어졌을때 할일들...
	}
}
