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

// TODO : 현재 send buffer 크기 이상의 객체에 대해 예외 처리가 안되어 있음. 즉 프로토콜 사용자가 알아서 자르고 지지고 레릿고 해야함
// TODO : 현재 is_last와 code등이 하드코딩 되어있음. Enum이나 함수로 바꾸는게 좋아 보임
public class Protocol {
    // 프로토콜 생성시 Builder 이용할 것
    public static class Builder {
        // 필수
        private final ProtocolType type; // 1바이트, 프로토콜 타입
        private final byte direction; // 1바이트, 프로토콜 응답 방향
        private final byte code_type; // 1바이트, 프로토콜 코드 종류
        private final byte code; // 1바이트, 프로토콜 코드
        // 옵션
        private short length = HEADER_LENGTH; // 2바이트, 전체 프로토콜 길이.
                                              // 실제 프로토콜에선 필수 정보지만 헤더길이는 10으로 고정되어 있으니
                                              // body 받고나면 길이 계산 가능해서 Builder에선 옵션임.
        private boolean is_splitted = false; // 1바이트, 프로토콜 분리 여부
        private boolean is_last = false; // 1바이트, 마지막 프로토콜인지 여부
        private short sequence = 0; // 2바이트, 시퀀스 넘버
        private Serializable body = null; // Body에 들어갈 객체
        private byte[] body_bytes = null; // body가 직렬화 된것

        public Builder(ProtocolType type, byte direction, byte code_type, byte code) {
            this.type = type;
            this.direction = direction;
            this.code_type = code_type;
            this.code = code;
        }

        public Builder sequence(short seq, boolean islast) {
            is_splitted = true;
            sequence = seq;
            is_last = islast;
            return this;
        }

        public Builder body(Serializable obj) throws IOException {
            body = obj;
            body_bytes = serialization(obj);
            this.length = (short) (HEADER_LENGTH + body_bytes.length);
            return this;
        }

        public Builder(byte[] packet) throws Exception {
            if (packet.length < HEADER_LENGTH)
                throw new Exception("패킷 길이가 먼가 짧다!!!");

            // 2바이트짜리 정보 만듬
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(packet[0]);
            bb.put(packet[1]);

            if (bb.getShort() != packet.length)
                throw new Exception("왜 패킷에 담긴 길이랑 실제 길이랑 다름???");

            this.length = bb.getShort();
            this.type = ProtocolType.getType(packet[2]);
            this.direction = packet[3];
            this.code_type = packet[4];
            this.code = packet[5];
            this.is_splitted = packet[6] == 0x00 ? false : true;
            this.is_last = packet[7] == 0x00 ? false : true;

            bb.clear();
            bb.put(packet[8]);
            bb.put(packet[9]);
            this.sequence = bb.getShort();

            this.body_bytes = Arrays.copyOfRange(packet, HEADER_LENGTH, packet.length);
            this.body = deserialization(this.body_bytes);
        }

        public Protocol build() throws IOException {
            return new Protocol(this);
        }
    }

    // Header
    public final short length; // 2바이트, 전체 프로토콜 길이
    public final ProtocolType type; // 1바이트, 프로토콜 타입
    public final byte direction; // 1바이트, 프로토콜 응답 방향
    public final byte code_type; // 1바이트, 프로토콜 코드 종류
    public final byte code; // 1바이트, 프로토콜 코드
    // 아래 3개는 body가 커져서 프로토콜 분리시 쓰임
    public final boolean is_splitted;// 1바이트, 프로토콜 분리 여부
    public final boolean is_last; // 1바이트, 마지막 프로토콜인지 여부
    public final short sequence; // 2바이트, 시퀀스 넘버
    // body 시작 인덱스를 알기 위해 자신의 길이를 가지고 있다.
    public static final int HEADER_LENGTH = 10;

    // Body
    public final Serializable body;
    public final byte[] body_bytes;

    // Builder로부터 프로토콜 생성
    protected Protocol(Builder builder) throws IOException {
        this.length = builder.length;
        this.type = builder.type;
        this.direction = builder.direction;
        this.code_type = builder.code_type;
        this.code = builder.code;
        this.is_splitted = builder.is_splitted;
        this.is_last = builder.is_last;
        this.sequence = builder.sequence;
        this.body = builder.body;
        this.body_bytes = builder.body_bytes;
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    // 순서는 변수 선언 순서와 같음
    public byte[] getPacket() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(length);
        baos.write(type.ordinal());
        baos.write(direction);
        baos.write(code_type);
        baos.write(code);
        baos.write(is_splitted ? 0x01 : 0x00);
        baos.write(is_last ? 0x01 : 0x00);
        baos.write(sequence);
        baos.write(body_bytes);
        return baos.toByteArray();
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