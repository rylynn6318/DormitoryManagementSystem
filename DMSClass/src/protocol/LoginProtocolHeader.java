package protocol;

// 로그인 프로토콜은 Base와 같은 구조임.
public class LoginProtocolHeader extends BaseHeader{

    protected LoginProtocolHeader(Builder<?> builder) {
        super(builder);
    }
}