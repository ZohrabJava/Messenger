package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConection(TCPConectionListener eventListener,String ipAddr,int port) throws IOException{
         this(eventListener,new  Socket(ipAddr,port));
    }

    public TCPConection( TCPConectionListener eventListener,Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket=socket;
        in=new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)) ;
        out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));

        rxThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConectionReady(TCPConection.this);
                    while (!rxThread.isInterrupted()){
                        eventListener.onReceveString(TCPConection.this,in.readLine());
                    }
                }catch (IOException e){
                    eventListener.onException(TCPConection.this,e);
                }finally {
                    eventListener.onDisconnect(TCPConection.this);
                }
            }
        });
        rxThread.start();
    }
    public synchronized void sendString(String value){
        try {
            out.write(value+"\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConection.this,e);
            disconnect();
        }
    }
    public synchronized void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
           eventListener.onException(TCPConection.this,e);
        }
    }

    @Override
    public String toString() {
        return "TCPConection: "+socket.getInetAddress()+": "+socket.getPort();
    }
}
