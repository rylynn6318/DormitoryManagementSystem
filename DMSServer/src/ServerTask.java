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
                Code1.FileType fileType = (Code1.FileType) protocol.code1;
                Code2.FileCode requestType = (Code2.FileCode) protocol.code2;
                Code2.FileCode isSuccess = Code2.FileCode.FAIL;

                switch (fileType){
                    case MEDICAL_REPORT:
                    case OATH:
                        switch (requestType){
                            case UPLOAD:
                                //     업로드 받았을때
                                //         받은 정보는 id와 파일
                                //         현재 학기 쿼리해와서 저장한다.
                                //         이름규칙은 [/파일종류/학기_학번.jpg]
                                //         id를 이용해 디비에 관련 정보 저장
                                //         성공 유무만 되돌려준다

                                // 여기 Logic 코드

                                try {
                                    socketHelper.write(
                                            new Protocol
                                                    .Builder(ProtocolType.FILE, Direction.TO_CLIENT, fileType, isSuccess)
                                                    .build()
                                    );
                                } catch (Exception e) {
                                    try {
                                        // 프로토콜 전송에는 에러떠도 파일 자체는 받았으니 isSuccess 전송
                                        socketHelper.write(
                                                new Protocol
                                                        .Builder(ProtocolType.FILE, Direction.TO_CLIENT, fileType, isSuccess)
                                                        .build()
                                        );
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }
                                break;
                            case REQUEST:
                                //     다운로드 요청 받았을때
                                //         일단 body를 통해 id만 온 상황
                                //         해당 id와 헤더에 있는 타입, 현재 학기로 서류 테이블 쿼리 수행한다.
                                //         결과가 있다면 관련 정보를 다 알수 있다.
                                //         해당 파일 소유자 학번과 파일을 리턴
                                //         body = <String, byte[]>
                                //         없다면 실패 리턴
                                //         body = null
                                //         클라이언트에서는 널체크 이후
                                //         로직 수행

                                String id = null;
                                try {
                                    id = (String) ProtocolHelper.deserialization(protocol.getBody());
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                String path = null;
                                File file = Paths.get(path).toFile();

                                try {
                                    socketHelper.write(
                                            new Protocol
                                                    .Builder(ProtocolType.FILE, Direction.TO_CLIENT, fileType, isSuccess)
                                                    .build()
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    try {
                                        socketHelper.write(
                                                new Protocol
                                                        .Builder(ProtocolType.FILE, Direction.TO_CLIENT, fileType, isSuccess)
                                                        .build()
                                        );
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                break;
                            case SUCCESS:
                            case FAIL:
                                default:
                                break;
                        }

                        break;
                    case CSV:
                        // CSV 파일의 경우 일방적인 전송밖에 없다
                        // body에는 csv 파일 바이트만 있는 상황
                        // 적당한곳에 csv파일 저장하고 로직 수행
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