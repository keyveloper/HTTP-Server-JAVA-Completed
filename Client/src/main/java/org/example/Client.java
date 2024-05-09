package org.example;

import lombok.Data;

import java.net.Socket;

@Data
public class Client{
    private final Socket socket = new Socket();
    private final String hostName;
    private ResponseReader clientPacketReceiver;
    private RequestSender clientRequestSender;

    public void run() {
        clientPacketReceiver = new ResponseReader(this);
        clientRequestSender = new RequestSender(this);
        Thread handlerThread = new Thread(clientPacketReceiver);
        handlerThread.start();
    }

    public void processCommand(String command) {
        sendRequest(CommandProcessor.extractRequest(command));
    }

    private void sendRequest(Request request) {
        clientRequestSender.sendRequest(request);
    }
}
