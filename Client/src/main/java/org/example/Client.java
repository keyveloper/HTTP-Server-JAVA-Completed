package org.example;

import lombok.Data;

import java.net.Socket;

@Data
public class Client{
    private final Socket socket = new Socket();
    private final String hostName;
    private ResponseReader responseReader;
    private RequestSender requestSender;

    public void run() {
        responseReader = new ResponseReader(this);
        requestSender = new RequestSender(this);
        Thread handlerThread = new Thread(responseReader);
        handlerThread.start();
    }

    public void processCommand(String command) {
        processRequest(CommandProcessor.extractRequest(command));
    }

    private void processRequest(Request request) {
        requestSender.divideRequest(request);
    }
}
