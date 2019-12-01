package protocol;

class FileProtocolHeader extends AbstractSplittableHeader {

    FileProtocolHeader(short length, byte type, byte direction, byte code) {
        super(length, type, direction, code);
    }
}