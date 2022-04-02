package chat.client;

import network.TCPConection;
import network.TCPConectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ClientWindow extends JFrame implements ActionListener, TCPConectionListener {
    private static final String IP_ADDR="169.254.73.209";
    private static final int PORT=8189;
    private static final int WIDTH=600;
    private static final int HEIGHT=400;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }
    private final JTextArea log=new JTextArea();
    private final JTextField fieldNickname =new JTextField("alex");
    private final JTextField fieldInput=new JTextField();

    private TCPConection conection;
    private ClientWindow()  {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput,BorderLayout.SOUTH);
        add(fieldNickname,BorderLayout.NORTH);
        setVisible(true);

        try {
            conection=new TCPConection(this,IP_ADDR,PORT);
        } catch (IOException e) {
            printMsg("Connection exception: "+e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg=fieldInput.getText();
        if(msg.equals(""))return;
        fieldInput.setText(null);
        conection.sendString(fieldNickname.getText()+": "+msg);
        printMsg(msg);
    }


    @Override
    public void onConectionReady(TCPConection tcpConection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceveString(TCPConection tcpConection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConection tcpConection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConection tcpConection, Exception e) {
        printMsg("Connection exception: "+e);
    }
    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
