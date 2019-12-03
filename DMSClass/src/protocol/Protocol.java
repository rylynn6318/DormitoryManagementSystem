package protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

// 프로토콜 쓰는법 (계획)
// 1. 보낼때 (로그인으로 예를 들겠음)
// Socket socket = new Socket("127.0.0.1", 666);
// OutputStream outputToServer = socket.getOutputStream();
// LoginInfo logininfo = new LoginInfo(id, pw); // LoginInfo 클래스는 Serializable 상속받음
//
// Protocol lp = Protocol.create(code, logininfo, ProtocolType.LOGIN); // code(혹은 page, event 등 코드적인 정보)와 body에 들어갈 클래스, tpye 넣으면 나머진 자동으로 만들어주게 할꺼임
// lp.send(outputToServer); or outputToServer.write(lp.getPacket());
// 단 두줄로 끝!
//
// 2. 받을때 (이번엔 서버가 받은 정보로 로그인 처리하는 대강의 예제)
// ServerSocket sSocket = new ServerSocket(666);
// Socket socket = sSocket.accept();
// InputStream inputFromClient = socket.getInputStream();
// byte[] buffer = new byte[우리프로토콜에서 이론상 최대 길이]; // 앞으로 데이터가 쓰일 버퍼
// inputFromClient.read(buffer); // 클라이언트로부터 정보 수신
// 
// Protocol lp = Protocol.create(buffer);
// LoginInfo logininfo = lp.getBody();
// 이 역시 앞의 과정 제외하면 두줄로 끝!
// 좀더 포멀하게 적자면
// Serializable body = protocol.getBody();
// 
// 파일처럼 분할되서 오면?
// 위에 buffer를
// int readBytes;
// while ((readBytes = inputFromClient.read(buffer)) != -1) {
//     fos.write(buffer, 0, readBytes); // 파일 쓰기
//     boas.write(buffer, 0, readBytes); // 바이트 배열 쓰기
// }
// 이런식으로 읽어온다음
// 바이트 첫번째 정보가 총 길이니 이걸 기준으로 짤라야함
// byte[] protocols = SomethingClass.getByteArrays(buffer);
// foreach(var protocol in protocols){
//     data += protocol.getBody();
// }
// 대충 이런 느낌으로? 처리해야 할듯?? 아직 구현 안해서 확답 못함ㅎ

// 자바에선 생성자에서 예외가 던져지네... 문제가 없을까?

public class Protocol {
    class Header {
        public final short length; // 2바이트, 전체 프로토콜 길이
        public final ProtocolType type; // 1바이트, 프로토콜 타입
        public final byte direction; // 1바이트, 프로토콜 응답 방향
        public final byte code; // 2바이트, 프로토콜 코드
        // 아래 3개는 body가 커져서 프로토콜 분리시 쓰임
        public final boolean isSplitted;// 1바이트, 프로토콜 분리 여부
        public final boolean isLast; // 1바이트, 마지막 프로토콜인지 여부
        public final short sequence; // 2바이트, 시퀀스 넘버
        // body 시작 인덱스를 알기 위해 자신의 길이를 가지고 있다.
        public static final int header_length = 10;

        Header(short length, ProtocolType type, byte direction, byte code) {
            this.length = length;
            this.type = type;
            this.direction = direction;
            this.code = code;
            this.header_length = this.getHeaderLength();
        }

        // 받은 패킷으로부터 프로토콜 헤더 추출
        static Header create(byte[] packet) {
            if (packet.length < 5)
                return null; // 먼가 짧은게 들어왔다!!

            // 아래는 공통적으로 쓰이는 헤더 부분
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(packet[0]);
            bb.put(packet[1]);
            short length = bb.getShort();
            // 자바는 byte[]의 길이를 바로 알아낼수 있어서 여기서 한번 더 체크해봄
            if (length != packet.length)
                return null; // 먼가 이상하다!
            ProtocolType type = ProtocolType.getType(packet[2]);
            byte direction = packet[3];
            byte code = packet[4];

            return create(length, type, direction, code);
        }

        // 새로 헤더 하나 만듬
        static Header create(short length, ProtocolType type, byte direction, byte code) {
            switch (type) {
            case UNDEFINED:
                // 이 경우로 프로토콜이 생성되는 경우는 없음!
                break;
            case LOGIN:
                // TODO : 구현해야함
            case FILE:
                // TODO : 구현해야함
            case EVENT:
                // TODO : 구현해야함
            default:
                // 이 경우로 프로토콜이 생성되면 안됨!
                break;
            }

            // 먼가 문제 생겨서 switch 내에서 return 안되면 null 리턴 == 예외다!
            // 따라서 null 체크 하셈ㅋ or 예외 던지게 코드 수정
            return null;
        }

        protected int getHeaderLength() {
            return 5;
        }

        byte[] getBytes() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(length);
            baos.write(type.ordinal());
            baos.write(direction);
            baos.write(code);
            return baos.toByteArray();
        }
    }

    // 프로토콜 생성시 Builder 이용할 것
    public static class Builder {
        // 필수
        private final short length; // 2바이트, 전체 프로토콜 길이
        private final ProtocolType type; // 1바이트, 프로토콜 타입
        private final byte direction; // 1바이트, 프로토콜 응답 방향
        private final byte code; // 2바이트, 프로토콜 코드
        // 옵션
        private boolean isSplitted = false; // 1바이트, 프로토콜 분리 여부
        private boolean isLast = false; // 1바이트, 마지막 프로토콜인지 여부
        private short sequence = 0; // 2바이트, 시퀀스 넘버

        public Builder(short length, ProtocolType type, byte direction, byte code) {
            this.length = length;
            this.type = type;
            this.direction = direction;
            this.code = code;
        }

        public Builder sequence(short seq, boolean islast) {
            isSplitted = true;
            sequence = seq;
            isLast = islast;
            return this;
        }

        public Protocol build() {
            return new Protocol(this);
        }
    }

    public final Header header;
    public final Serializable body;
    protected final byte[] body_bytes;

    public Protocol(Builder builder) {

    }

    // 받은 패킷으로부터 새 프로토콜 만들고 head 할당함.
    public Protocol(byte[] packet) throws IOException, ClassNotFoundException {
        this.header = Header.create(packet);
        this.body_bytes = Arrays.copyOfRange(packet, header.header_length, packet.length);
        this.body = deserialization(this.body_bytes);
    }

    // 적당한 정보 받아서 새 프로토콜 만듬 ( 전송용 )
    public Protocol(short length, ProtocolType type, byte direction, byte code, Serializable obj) throws IOException {
        this.body_bytes = serialization(obj);
        this.body = obj;
        this.header = Header.create(length, type, direction, code);
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    public byte[] getPacket() {
        byte[] headbyte = header.getBytes();
        byte[] packet = new byte[headbyte.length + body_bytes.length];
        System.arraycopy(headbyte, 0, packet, 0, headbyte.length);
        System.arraycopy(body_bytes, 0, packet, headbyte.length, body_bytes.length);
        return packet;
    }

    private static byte[] serialization(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
                return baos.toByteArray();
            }
        }
    }

    // body에 있는 바이트 배열을 역직렬화해서 Serializable 객체 반환. 이후 캐스팅은 알아서 하셈.
    private static Serializable deserialization(byte[] bytes) throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object obj = ois.readObject();
                return (Serializable) obj;
            }
        }
    }
}