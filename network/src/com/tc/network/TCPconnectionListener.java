package com.tc.network;

public interface TCPconnectionListener {

    void onConnectionReady(TCPconnection tcPconnection);
    void onReceiveString(TCPconnection tcPconnection, String value);
    void onDisconnect(TCPconnection tcPconnection);
    void onException(TCPconnection tcPconnection, Exception e);
}
