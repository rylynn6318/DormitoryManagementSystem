package protocol;

public abstract class AbstractProtocol{
    private byte[] packet;

    AbstractProtocol(byte[] packet){
        this.packet = packet;
    }

    public abstract IProtocolable toObject();
}