package protocol;

import protocol.AbstractProtocol;

// LoginProtocol
//  로그인을 할때 쓰는 프로토콜.
// ┌──────── LOGIN ────────┐ 
// ├───────┬───────────────┤ 
// │ 0 ~ 1 │ Length        │  
// ├───────┼───────────────┤ 
// │   2   │ Type          │
// ├───────┼───────────────┤ 
// │   3   │ Direction     │
// ├───────┼───────────────┤ 
// │   4   │ Code          │
// ├───────┼───────────────┤ 
// │   5~  │ Body          │
// └───────┴───────────────┘ 

public class LoginProtocol extends AbstractProtocol {

    protected LoginProtocol(byte[] packet) {
        super(packet);
    }

}