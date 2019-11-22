package protocol;

class ProtocolHeader{
    public static final int SIZE_TYPE = 8;
    
    private byte[] type = new byte[SIZE_TYPE];

    public ProtocolType type(){
        return ProtocolType.getType();
    }
}