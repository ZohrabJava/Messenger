package chat.server;

import network.TCPConection;
import network.TCPConectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConectionListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConection> conections=new ArrayList<>();
    private ChatServer(){
        System.out.println("Server running...");
        try (ServerSocket serverSocket=new ServerSocket(8189);){
            while (true){
                try {
                    new TCPConection(this,serverSocket.accept());
                }catch (IOException e){
                    System.out.println("TCPConectiion exception: " +e );
                }
            }
        } catch (IOException e) {
           throw new RuntimeException();
        }
    }

    @Override
    public synchronized void onConectionReady(TCPConection tcpConection) {
        conections.add((tcpConection));
        sendToAllConections("Client conected: "+ tcpConection);
    }

    @Override
    public synchronized void onReceveString(TCPConection tcpConection, String value) {

    }

    @Override
    public synchronized void onDisconnect(TCPConection tcpConection) {
        conections.remove(tcpConection);
        System.out.println("TCPConection disconect: "+ tcpConection);
    }

    @Override
    public synchronized void onException(TCPConection tcpConection, Exception e) {
        System.out.println("TCPConection exception: "+e);
    }
    private void sendToAllConections(String value){
        System.out.println(value);
        final int cnt=conections.size();
        for (int i = 0; i <cnt ; i++) {
            conections.get(i).sendString(value);
        }
    }
}
