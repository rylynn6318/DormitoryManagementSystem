package protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
    public final AbstractHeader header;
    private final byte[] body_bytes;
    public final Serializable body;

    // 받은 패킷으로부터 새 프로토콜 만들고 head 할당함.
    public Protocol(byte[] packet) throws IOException, ClassNotFoundException {
        this.header = AbstractHeader.create(packet);
        this.body_bytes = Arrays.copyOfRange(packet, header.header_length, packet.length);
        this.body = deserialization(this.body_bytes);
    }

    // 적당한 정보 받아서 새 프로토콜 만듬 ( 전송용 )
    public Protocol(short length, ProtocolType type, byte direction, byte code, Serializable obj) throws IOException {
        this.body_bytes = serialization(obj);
        this.body = obj;
        this.header = AbstractHeader.create(length, (byte)type.ordinal(), direction, code);
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    public byte[] getPacket() {
        byte[] headbyte = header.getBytes();
        byte[] packet = new byte[headbyte.length + body_bytes.length];
        System.arraycopy(headbyte, 0, packet, 0, headbyte.length);
        System.arraycopy(body_bytes, 0, packet, headbyte.length, body_bytes.length);
        return packet;
    }

    public AbstractHeader getHeader() {
        return header;
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