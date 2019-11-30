package protocol;

class FileProtocolHeader extends AbstractSplitableHeader {

    FileProtocolHeader(short length, byte type, byte direction, byte code) {
        super(length, type, direction, code);
    }
}