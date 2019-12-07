import enums.*;
import models.Account;
import ultils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//클라이언트와 연결을 담당하는 쓰레드
//서버소켓을 생성하고, 서버소켓이 클라이언트와 연결을 accept 하면
//해당 클라이언트에게 새 ReceiveThread와 SendThread를 만들어 준다.

public class ServerTask implements Runnable {
    private int port;
    ServerSocket serverSocket;
    SocketHelper socketHelper;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ServerTask(SocketHelper socketHelper) {
        this.socketHelper = socketHelper;
    }

    @Override
    public void run() {
        //NetworkHandler 에서 종료요청이 오기전까지 계속 클라이언트의 요청을 받아들인다.
        Protocol protocol = null;
        try {
            protocol = socketHelper.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (protocol.type) {
            case LOGIN:
                try {
                    if (((Account) ProtocolHelper.deserialization(protocol.getBody())).accountId.equals("admin")) {
                        try {
                            socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT, Code1.NULL, Code2.LoginResult.ADMIN).build());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT, Code1.NULL, Code2.LoginResult.STUDENT).build());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case FILE:
                //file 처리
                break;
            case EVENT:
                //event 처리
                break;
        }

    }

    public void close() {
        System.out.println("클라이언트스레드풀 종료 시작");

        threadPool.shutdown();
        System.out.println("클라이언트스레드풀 종료됨");
    }
}