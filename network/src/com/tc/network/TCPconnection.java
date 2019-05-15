package com.tc.network;

import java.io.*;
import java.net.Socket;

public class TCPconnection {

    private final Socket socket;
    private final Thread receiveThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPconnectionListener tcPconnectionListener;

    public TCPconnection(TCPconnectionListener tcPconnectionListener, String ip, int port) throws IOException {
        this(tcPconnectionListener, new Socket(ip, port));

    }

    public TCPconnection(TCPconnectionListener tcPconnectionListener, Socket socket) throws IOException {
        this.tcPconnectionListener = tcPconnectionListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tcPconnectionListener.onConnectionReady(TCPconnection.this);

                    while(!receiveThread.isInterrupted()){
                        tcPconnectionListener.onReceiveString(TCPconnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    tcPconnectionListener.onException(TCPconnection.this, e);
                } finally{
                    tcPconnectionListener.onDisconnect(TCPconnection.this);
                }
            }
        });
        receiveThread.start();
    }

    public synchronized void sendString(String value) {
        try{
            out.write(value + "\r\n");
            out.flush();
        } catch(IOException e){
            tcPconnectionListener.onException(TCPconnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        receiveThread.interrupt();
        try{
            socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return "TCPconnection" + socket.getInetAddress() + ":" + socket.getPort();
    }
}
