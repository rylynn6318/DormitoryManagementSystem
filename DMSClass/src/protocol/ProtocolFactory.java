package protocol;

public class ProtocolFactory
{
    public static AbstractProtocol createProtocol(byte[] input)
    {
        ProtocolType input_type = ProtocolType.getType(input[0]);

        switch (input_type) {
            
        }
        return null;
    }
}