package protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// 프로토콜 쓰는법
// 1. 보낼때 (로그인으로 예를 들겠음)
// Socket socket = new Socket("127.0.0.1", 666);
// OutputStream outputToServer = socket.getOutputStream();
// LoginInfo logininfo = new LoginInfo(id, pw); // LoginInfo 클래스는 Serializable 상속받음
//
// LoginProtocol lp = LoginProtocol.create(code, logininfo); // code(혹은 page, event 등 코드적인 정보)와 body에 들어갈 클래스만 넣으면 나머진 자동으로 만들어줌
// outputToServer.write(lp.getPacket());
// 단 두줄로 끝!
//
// 2. 받을때 (이번엔 서버가 받은 정보로 로그인 처리하는 대강의 예제)
// ServerSocket sSocket = new ServerSocket(666);
// Socket socket = sSocket.accept();
// InputStream inputFromClient = socket.getInputStream();
// byte[] buffer = new byte[우리프로토콜에서 이론상 최대 길이]; // 앞으로 데이터가 쓰일 버퍼
// inputFromClient.read(buffer); // 클라이언트로부터 정보 수신
// 
// LoginProtocol lp = LoginProtocol.create(buffer);
// LoginInfo logininfo = lp.getBody();
// 이 역시 앞의 과정 제외하면 두줄로 끝!
// 좀더 포멀하게 적자면
// AbstractProtocol protocol = AbstractProtocol.create(buffer);
// Serializable body = protocol.getBody();

public abstract class AbstractProtocol {
    private BaseHeader header;
    private byte[] body;

    // 새 프로토콜 만들고 head 할당함.
    // 생성자는 쓰지말고 create로 만드셈.
    protected AbstractProtocol() {
    }

    // 패킷으로부터 프로토콜을 만듬.
    // @Builder까진 필요없을듯
    public static AbstractProtocol create(byte[] packet) {

        return null;
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    public byte[] getPacket() {
        byte[] headbyte = header.getBytes();
        byte[] packet = new byte[headbyte.length + body.length];
        System.arraycopy(headbyte, 0, packet, 0, headbyte.length);
        System.arraycopy(body, 0, packet, headbyte.length, body.length);
        return packet;
    }

    public BaseHeader getHeader(){
        return header;
    }

    // Serializable 객체를 직렬화 해서 body에 넣음.
    public void setBody(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
                body = baos.toByteArray();
            }
        }
    }

    // body에 있는 바이트 배열을 역직렬화해서 Serializable 객체 반환. 이후 캐스팅은 알아서 하셈.
    public Serializable getBody() throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object obj = ois.readObject();
                return (Serializable) obj;
            }
        }
    }
}