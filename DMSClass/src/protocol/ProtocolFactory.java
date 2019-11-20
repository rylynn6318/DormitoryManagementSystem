package protocol;

public class ProtocolFactory
{
    public static IProtocol createProtocol(byte[] input)
    {
        ProtocolType input_type = ProtocolType.getType(input[0]);

        switch (input_type) {
            
        }
    }
}