import enums.ProtocolType;

import java.net.InetAddress;
import java.util.Date;

public enum Logger {
    INSTANCE;

    public synchronized void print(String msg){
        System.out.println(new Date() + " " + msg);
    }

    public synchronized void print(InetAddress address, String msg){
        System.out.println(new Date() + " [ " + address + " ] " + msg);
    }

    public synchronized void print(InetAddress address, ProtocolType type){
        System.out.println(new Date() + " [ " + address + " ] 요청 : " + type.name());
    }
}
