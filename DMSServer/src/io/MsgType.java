package io;

//메시지 타입에 대한 Enum
//IOHandler에서 printMsg를 사용했을 때 각 타입에 맞게 헤더가 붙는다.
//또한 메시지 타입이 DEBUG인 경우 디버그모드로 실행했을 때만 메시지가 표시된다.
public enum MsgType
{
	UNKNOWN, GENERAL, CAUTION, ERROR, DEBUG
}
