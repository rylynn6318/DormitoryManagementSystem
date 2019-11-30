package protocol;

import protocol.AbstractProtocol;

// LoginProtocol
//  로그인을 할때 쓰는 프로토콜.
// ┌──────── LOGIN ────────┐ 시작 인덱스
// ├───────┬───────────────┤  0
// │   0   │ ProtocolType  │  
// ├───────┼───────────────┤  LEN_PROTOCOL_TYPE
// │ 1~21  │ Login Id      │
// ├───────┼───────────────┤  LEN_LOGIN_ID
// │ 22~40 │ Login PW      │
// └───────┴───────────────┘ 최종 길이
//							  LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_LOGIN_PASSWORD

public class LoginProtocol extends AbstractProtocol {

    protected LoginProtocol(byte[] packet) {
        super(packet);
    }

}