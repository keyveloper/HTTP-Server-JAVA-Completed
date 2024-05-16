package org.example;

import lombok.Data;

import java.net.Socket;

@Data
public class Client{
    private final Socket socket = new Socket();
    private final String hostName;
    private ResponseReader responseReader;
    private RequestSender requestSender;
    private CommandProcessor commandProcessor = new CommandProcessor(this);

    public void run() {
        responseReader = new ResponseReader(this);
        requestSender = new RequestSender(this);
        Thread handlerThread = new Thread(responseReader);
        handlerThread.start();
    }

    public void processCommand(String command) {
        Request request = commandProcessor.extractRequest(command);
        if (request == null) {
            // null -> wrong command
            System.out.println("Wrong Command");
        } else {
            byte[] requestPacket = HttpRequestMaker.makeHttpRequest(request);
            requestSender.sendHttpRequest(requestPacket);
        }
    }

}
