package protocol;

class EventProtocolHeader extends AbstractSplitableHeader{

    EventProtocolHeader(short length, byte type, byte direction, byte code) {
        super(length, type, direction, code);
    }
}