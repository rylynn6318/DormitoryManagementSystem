package protocol;

class EventProtocolHeader extends AbstractSplittableHeader {

    EventProtocolHeader(short length, byte type, byte direction, byte code, byte codeType) {
        super(length, type, direction, code, codeType);
        // TODO Auto-generated constructor stub
    }

}