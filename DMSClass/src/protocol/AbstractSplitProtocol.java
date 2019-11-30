package protocol;

// 로그인 프로토콜이나 파일 전송 프로토콜이나
// 결국 코드가 2개 들어가고 분할될 수 있다는 헤더 구조는 같기에
// 이 클래스를 상속받은다음 생성자랑 이름만 조금 바꿔서 사용함
abstract class AbstractSplitProtocol extends AbstractProtocol{

}