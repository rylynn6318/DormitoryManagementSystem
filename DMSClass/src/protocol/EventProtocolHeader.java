package protocol;

class EventProtocolHeader extends AbstractSplittableHeader {

    EventProtocolHeader(short length, byte type, byte direction, byte code) {
        super(length, type, direction, code);
    }
}