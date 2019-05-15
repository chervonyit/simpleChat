package com.tc.chat.server;

import com.tc.network.TCPconnection;
import com.tc.network.TCPconnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPconnectionListener {

    public static void main(String[] args) throws IOException {
        new ChatServer();
    }

    private final ArrayList<TCPconnection> connections = new ArrayList<>();

    private ChatServer() throws IOException {
        System.out.println("Server running");
        try(ServerSocket serverSocket = new ServerSocket(8189)){
            while(true){
                try{
                    new TCPconnection(this, serverSocket.accept());
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPconnection tcPconnection) {
        connections.add(tcPconnection);
        sendToAll("New client connected: " + tcPconnection);
    }

    @Override
    public synchronized void onReceiveString(TCPconnection tcPconnection, String value) {
        sendToAll(value);
    }

    @Override
    public synchronized void onDisconnect(TCPconnection tcPconnection) {
        connections.remove(tcPconnection);
        sendToAll("Client disconnected: " + tcPconnection);
    }

    @Override
    public synchronized void onException(TCPconnection tcPconnection, Exception e) {
        System.out.println("Exception occurs: " + e);
    }

    private void sendToAll(String value){
        System.out.println(value);
        int connectionSize = connections.size();
        for (int i = 0; i < connectionSize; i++) {
            connections.get(i).sendString(value);
        }
    }
}
