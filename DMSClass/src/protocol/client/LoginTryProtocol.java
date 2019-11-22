package protocol.client;
import protocol.AbstractProtocol;

// LoginProtocol
//  클라이언트가 서버의 로그인을 할때 쓰는 프로토콜.
// ┌ LOGIN_RESPONSE_ID, PW ┐ 시작 인덱스
// ├───────┬───────────────┤  0
// │   0   │ ProtocolType  │  
// ├───────┼───────────────┤  LEN_PROTOCOL_TYPE
// │ 1~21  │ Login Id      │
// ├───────┼───────────────┤  LEN_LOGIN_ID
// │ 22~40 │ Login PW      │
// └───────┴───────────────┘ 최종 길이
//							  LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_LOGIN_PASSWORD

public class LoginTryProtocol extends AbstractProtocol {

    LoginTryProtocol(byte[] packet) {
        super(packet);
        // TODO Auto-generated constructor stub
    }

}