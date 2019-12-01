package protocol;

// 로그인 프로토콜은 Base와 같은 구조임.
class LoginProtocolHeader extends AbstractHeader {

	LoginProtocolHeader(short length, byte type, byte direction, byte code) {
		super(length, type, direction, code);
	}
}