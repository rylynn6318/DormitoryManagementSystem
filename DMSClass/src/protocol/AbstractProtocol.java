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
// LoginProtocol lp = LoginProtocol.create(code, logininfo); // code(혹은 page, event 등 코드적인 정보)와 body에 들어갈 클래스만 넣으면 나머진 자동으로 만들어주게 할꺼임
// lp.send(outputToServer);
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

// 사실상 헤더만 바뀌고 프로토콜은 다 똑같은데 왜 여러개 상속해서 쓰느냐?
// 1. File 같은 경우 분할해야 하는데 File 클래스만 넘겨주면 알아서 분할해서 보내고 받게 할려고
// 2. 헤더에 타입 등의 정보를 자동으로 넣게 할려고
// 생각해보니 굳이 안 나누고 헤더만 분리해서 써도 될것 같기도 하고;;;;
// 근데 분할되서 온걸 하나의 프로토콜로 합쳐서 처리할려면 AbstractSplittableProtocol 만들어서 거기도 또 처리하던가 해야할듯;;;;
// 않의 근데 소켓 하나로 통신할껀데 이러면 두번째로 보내는거부턴 헤더 빼고 보내도 되는거 아닌가?
// 어짜피 리턴 받을때까지 클라이언트 블락 걸어서 데이터 또 못보내게 하면 다른 프로토콜 오는것도 없을텐데
// 애초에 요청 하나에 소켓 하나라서 진짜 헤더 더 안보내도 될듯; 월요일에 선명킴한테 물어봐야지
// 만약 그렇다면 AbstractSplittableHeader를 쓸 필요도 없을듯

// 위에 의식의 흐름대로 주석(시진핑 아님) 쓰고 든 생각 정리
// 1. stateless 서버라면 한번에 하나의 프로토콜만 전송된다. 소켓 레벨에서 다른 프로토콜이랑 썪일 이유가 없다. 그렇다면 분할되는 프로토콜에 헤더 넣어줄 필요가 있나?
//    즉, [header|body] + [header|body] + ... 이렇게 말고 [header|body] + [body] + [body] 이렇게 보내도 되는거 아닌가? 내가 잘못 알고 있나?
// 2. 프로토콜을 받는건 그렇다 치고, 보낼때 Protocol 타입을 명시 해야할까? Header만 만들어서 넣어주면 body엔 머가 드가든 상관 없지 않나?
//    일단 Header는 확실히 type에 따라 클래스를 나눠야한다. 여기서 Protocol까지 나눠야 할 이유가 있을까

public abstract class AbstractProtocol {
    private AbstractHeader header;
    private byte[] body;

    // 새 프로토콜 만들고 head 할당함.
    // 일단은 protected로 만듬 2019.12.01 05:31
    protected AbstractProtocol(byte[] packet) {
        this.header = AbstractHeader.create(packet);
        this.body = Arrays.copyOfRange(packet, header.getHeaderLength(), packet.length);
    }

    // head를 바이트 배열로 바꿔서 이를 body랑 합쳐 반환함.
    public byte[] getPacket() {
        byte[] headbyte = header.getBytes();
        byte[] packet = new byte[headbyte.length + body.length];
        System.arraycopy(headbyte, 0, packet, 0, headbyte.length);
        System.arraycopy(body, 0, packet, headbyte.length, body.length);
        return packet;
    }

    public AbstractHeader getHeader() {
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