import enums.*;

import logic.LoginChecker;
import models.Account;
import utils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//클라이언트와 연결을 담당하는 쓰레드
//서버소켓을 생성하고, 서버소켓이 클라이언트와 연결을 accept 하면
//해당 클라이언트에게 새 ReceiveThread와 SendThread를 만들어 준다.

public class ServerTask implements Runnable {
    SocketHelper socketHelper;

    public ServerTask(SocketHelper socketHelper) {
        this.socketHelper = socketHelper;
    }

    @Override
    public void run() {
        Protocol protocol = null;
        try {
            protocol = socketHelper.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (protocol.type) {
            case LOGIN:
                Account result = null;
                try {
                    result = LoginChecker.check((Account) ProtocolHelper.deserialization(protocol.getBody()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT, Code1.NULL, Code2.LoginResult.get(result.userType)).build());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case FILE:
                //file 처리
                break;
            case EVENT:
                //event 처리
            	switch((Code1.Page) protocol.code1)
            	{
            	case 입사신청:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.student_submitApplicationPage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CANCEL:
            			try {
            				Responser.student_submitApplicationPage_onCancel(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	}
                break;
        }
    }
}