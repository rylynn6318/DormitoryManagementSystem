import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.server.ExportException;

import enums.*;
import logic.*;
import models.*;
import utils.*;

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
                try {
                    Account result = LoginChecker.check((Account) ProtocolHelper.deserialization(protocol.getBody()));
                    socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT, Code1.NULL, Code2.LoginResult.get(result.userType)).build());
                } catch (Exception e) {
                    try {
                        // 먼가 예외 터지면 일단 실패로 전송함
                        socketHelper.write(new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_CLIENT, Code1.NULL, Code2.LoginResult.FAIL).build());
                    } catch (IOException ex) {
                        // 이것조차도 터질수 있긴 한데 이 경우는 안터질꺼임
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
                break;
            case FILE:
                switch ((Code2.FileCode) protocol.code2) {
                    case UPLOAD:
                        try {
                            ProtocolHelper.downloadFileFrom(protocol);
                            // 잘 받았으면 body 에 Bool.TRUE 담아서 전송
                            socketHelper.write(
                                    new Protocol
                                            .Builder(ProtocolType.FILE, Direction.TO_CLIENT, protocol.code1, Code2.FileCode.UPLOAD_RESULT)
                                            .body(ProtocolHelper.serialization(Bool.TRUE))
                                            .build()
                            );
                        } catch (Exception e) {
                            // 먼가 실패했을 경우 body 에 Bool.FALSE 담아서 전송
                            try {
                                socketHelper.write(
                                        new Protocol
                                                .Builder(ProtocolType.FILE, Direction.TO_CLIENT, protocol.code1, Code2.FileCode.UPLOAD_RESULT)
                                                .body(ProtocolHelper.serialization(Bool.FALSE))
                                                .build()
                                );
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        break;
                    case REQUEST_DOWNLOAD:
                        try {
                            String path = (String) ProtocolHelper.deserialization(protocol.getBody());
                            File file = Paths.get(path).toFile();
                            socketHelper.write(
                                    new Protocol
                                            .Builder(ProtocolType.FILE, Direction.TO_CLIENT, protocol.code1, Code2.FileCode.UPLOAD_RESULT)
                                            .build()
                            );
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case UPLOAD_RESULT:
                    case DOWNLOAD:
                    default:
                        // 이 경우는 클라이언트에서 올일 없다!
                        break;
                }
                break;
            case EVENT:
                //event 처리
                switch ((Code1.Page) protocol.code1) {
                    case 입사신청:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.student_submitApplicationPage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SUBMIT:
                            	try {
                                    Responser.student_submitApplicationPage_onSubmit(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CANCEL:
                                try {
                                    Responser.student_submitApplicationPage_onCancel(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 신청조회:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.student_CheckApplicationPage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.student_CheckApplicationPage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 고지서조회:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.student_CheckBillPage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.student_CheckBillPage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 호실조회:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.student_checkRoomPage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.student_checkRoomPage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 서류제출:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.student_submitDocumentPage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 서류조회:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.student_checkDocumentPage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.student_checkDocumentPage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 선발일정관리:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.admin_scheduleManagePage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.admin_scheduleManagePage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DELETE:
                                try {
                                    Responser.admin_scheduleManagePage_onDelete(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SUBMIT:
                                try {
                                    Responser.admin_scheduleManagePage_onInsert(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 생활관관리:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.admin_dormitoryManagePage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.admin_dormitoryManagePage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DELETE:
                                try {
                                    Responser.admin_dormitoryManagePage_onDelete(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SUBMIT:
                                try {
                                    Responser.admin_dormitoryManagePage_onInsert(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 입사선발자관리:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.admin_selecteesManagePage_onSelection(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.admin_selecteesManagePage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DELETE:
                                try {
                                    Responser.admin_selecteesManagePage_onDelete(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SUBMIT:
                                try {
                                    Responser.admin_dormitoryManagePage_onInsert(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 입사자관리:
                        switch ((Code2.Event) protocol.code2) {
                            case ASSIGN:
                                try {
                                    Responser.admin_boarderManagePage_onAllocate(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.admin_boarderManagePage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DELETE:
                                try {
                                    Responser.admin_boarderManagePage_onDelete(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SUBMIT:
                                try {
                                    Responser.admin_boarderManagePage_onInsert(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 납부관리:
                        switch ((Code2.Event) protocol.code2) {
                            case CHECK:
                                try {
                                    Responser.admin_paymentManagePage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case UPDATE:
                                try {
                                    Responser.admin_paymentManagePage_onUpdate(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 서류관리:
                        switch ((Code2.Event) protocol.code2) {
                            case REFRESH:
                                try {
                                    Responser.admin_documentManagePage_onEnter(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case CHECK:
                                try {
                                    Responser.admin_documentManagePage_onCheck(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DELETE:
                                try {
                                    Responser.admin_documentManagePage_onDelete(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case UPDATE:
                                try {
                                    Responser.admin_documentManagePage_onUpdate(protocol, socketHelper);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                }
        }

        try {
            socketHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.INSTANCE.print(socketHelper.getInetAddress(), "요청 반환, 연결 종료");
    }
}