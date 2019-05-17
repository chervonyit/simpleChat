package com.tc.chat.client;

import com.tc.network.TCPconnection;
import com.tc.network.TCPconnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatClient extends JFrame implements ActionListener, TCPconnectionListener {

    private static final String ipAddr = "localhost";
    private static final int port = 8189;
    private static final int width = 800;
    private static final int heigth = 600;

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClient();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField name = new JTextField();
    private final JTextField input = new JTextField();
    private TCPconnection tcpConnection;


    private ChatClient() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, heigth);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        input.addActionListener(this);
        add(input, BorderLayout.SOUTH);
        add(name, BorderLayout.NORTH);

        setVisible(true);
        try {
            tcpConnection = new TCPconnection(this, ipAddr, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = input.getText();
        if(message.equals(""))return;
        input.setText(null);
        tcpConnection.sendString(name.getText() + " : " + message);
    }

    @Override
    public void onConnectionReady(TCPconnection tcPconnection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPconnection tcPconnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPconnection tcPconnection) {
        printMessage("Disconnected");
    }

    @Override
    public void onException(TCPconnection tcPconnection, Exception e) {
        printMessage("Exception: " + e);
    }

    private synchronized void  printMessage(String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(message + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
