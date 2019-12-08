import java.io.IOException;

import enums.Code1;
import enums.Code2;
import enums.Direction;
import enums.ProtocolType;
import logic.LoginChecker;
import models.Account;
import utils.Protocol;
import utils.ProtocolHelper;
import utils.SocketHelper;

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
        Logger.INSTANCE.print(socketHelper.getInetAddress(), "연결");

        Protocol protocol = null;
        try {
            protocol = socketHelper.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.INSTANCE.print(socketHelper.getInetAddress(), protocol.type);

        switch (protocol.type) {
            case LOGIN:
                Account result = null;
                try {
                    result = LoginChecker.check((Account) ProtocolHelper.deserialization(protocol.getBody()));
                } catch (ClassNotFoundException e) {
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
        					Responser.student_submitApplicationPage_onSubmit(protocol, socketHelper);
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
            	case 신청조회:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.student_CheckApplicationPage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CANCEL:
            			try {
            				Responser.student_CheckApplicationPage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 고지서조회:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.student_CheckBillPage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.student_CheckBillPage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 호실조회:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.student_checkRoomPage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.student_checkRoomPage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 서류제출:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.student_submitDocumentPage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		}
            		break;
            	case 서류조회:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.student_checkDocumentPage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.student_checkDocumentPage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 선발일정관리:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.admin_scheduleManagePage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.admin_scheduleManagePage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case DELETE:
            			try {
            				Responser.admin_scheduleManagePage_onDelete(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case SUBMIT:
            			try {
            				Responser.admin_scheduleManagePage_onInsert(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 생활관관리:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.admin_dormitoryManagePage_onEnter(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.admin_dormitoryManagePage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case DELETE:
            			try {
            				Responser.admin_dormitoryManagePage_onDelete(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case SUBMIT:
            			try {
            				Responser.admin_dormitoryManagePage_onInsert(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 입사선발자관리:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try
						{
        					Responser.admin_selecteesManagePage_onSelection(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.admin_selecteesManagePage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case DELETE:
            			try {
            				Responser.admin_selecteesManagePage_onDelete(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case SUBMIT:
            			try {
            				Responser.admin_dormitoryManagePage_onInsert(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 입사자관리:
            		switch((Code2.Event) protocol.code2)
            		{
            		case ASSIGN:
            			try
						{
        					Responser.admin_boarderManagePage_onAllocate(protocol, socketHelper);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            			break;
            		case CHECK:
            			try {
            				Responser.admin_boarderManagePage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case DELETE:
            			try {
            				Responser.admin_boarderManagePage_onDelete(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case SUBMIT:
            			try {
            				Responser.admin_boarderManagePage_onInsert(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 납부관리:
            		switch((Code2.Event) protocol.code2)
            		{
            		case CHECK:
            			try {
            				Responser.admin_paymentManagePage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case UPDATE:
            			try {
            				Responser.admin_paymentManagePage_onUpdate(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
            	case 서류관리:
            		switch((Code2.Event) protocol.code2)
            		{
            		case REFRESH:
            			try {
            				Responser.admin_documentManagePage_onEnter(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case CHECK:
            			try {
            				Responser.admin_documentManagePage_onCheck(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case DELETE:
            			try {
            				Responser.admin_documentManagePage_onDelete(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		case UPDATE:
            			try {
            				Responser.admin_documentManagePage_onUpdate(protocol, socketHelper);
            			} catch(Exception e) {
            				e.printStackTrace();
            			}
            			break;
            		}
            		break;
        }

        try {
            socketHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.INSTANCE.print(socketHelper.getInetAddress(), "요청 반환, 연결 종료");
    }
}