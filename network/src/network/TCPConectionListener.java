package network;

public interface TCPConectionListener {
    void onConectionReady(TCPConection tcpConection);
    void onReceveString(TCPConection tcpConection,String value);
    void onDisconnect(TCPConection tcpConection);
    void  onException(TCPConection tcpConection,Exception e);
}
