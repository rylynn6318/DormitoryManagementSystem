package protocol;

class EventProtocolHeader extends BaseHeader{

    EventProtocolHeader(short length, byte type, byte direction, byte code) {
        super(length, type, direction, code);
    }
    
}