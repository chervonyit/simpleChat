package com.tc.chat.client;

import com.tc.network.TCPconnection;
import com.tc.network.TCPconnectionListener;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class JavaFXChat extends Application implements TCPconnectionListener {


    private static final String ipAddr = "tcstorage.myqnapcloud.com";
    private static final int port = 8189;
    private TCPconnection tcpConnection;
    private TextField nameField;
    private TextField messageField;
    private TextArea chatArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent rootотм = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Simple chat");
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);

        Label lbl = new Label("Simple chat");
        lbl.setFont(Font.font("Amble CN", FontWeight.BOLD, 24));
        vb.getChildren().add(lbl);

        Label nameLbl = new Label("Name: ");
        nameField = new TextField();
        vb.getChildren().addAll(nameLbl, nameField);

        chatArea = new TextArea();
        chatArea.setEditable(false);
        vb.getChildren().add(chatArea);

        Label messageLbl = new Label("Message: ");
        messageField = new TextField();
        vb.getChildren().addAll(messageLbl, messageField);

        final Button sendBtn = new Button();
        sendBtn.setText("Send");
        vb.getChildren().add(sendBtn);

        tcpConnection = new TCPconnection(this, ipAddr, port);

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                String messageText = nameField.getText() + " : " + messageField.getText();
                if(!messageField.getText().equals("")){
                    tcpConnection.sendString(messageText);
                } else {
                    return;
                }
            }
        });

        primaryStage.setScene(new Scene(vb, 300, 275));
        primaryStage.show();
    }

    public void printMsg(String message) {
        chatArea.appendText(message + "\r\n");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void onConnectionReady(TCPconnection tcPconnection) {
        printMsg("Connected...");
    }

    public void onReceiveString(TCPconnection tcPconnection, String value) {
        printMsg(value);
    }

    public void onDisconnect(TCPconnection tcPconnection) {
        printMsg("Disconnected...");
    }

    public void onException(TCPconnection tcPconnection, Exception e) {
        printMsg("Exception occurs");
    }
}
