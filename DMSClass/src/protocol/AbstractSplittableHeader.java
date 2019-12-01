package protocol;

import java.io.ByteArrayOutputStream;

// 로그인 프로토콜이나 파일 전송 프로토콜이나
// 결국 코드가 2개 들어가고 분할될 수 있다는 헤더 구조는 같기에
// 이 클래스를 상속받은다음 생성자랑 이름만 조금 바꿔서 사용함
abstract class AbstractSplittableHeader extends AbstractHeader {
    protected final byte codeType;
    protected final boolean isSplitted;
    protected final boolean isLast;
    protected final short sequence;

    AbstractSplittableHeader(short length, byte type, byte direction, byte code) {
        super(length, type, direction, code);
    }

    @Override
    byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] tmp = super.getBytes();
        baos.write(tmp, 0, tmp.length);
        baos.write(length);
        baos.write(type);
        baos.write(direction);
        baos.write(code);
        return baos.toByteArray();
    }
}