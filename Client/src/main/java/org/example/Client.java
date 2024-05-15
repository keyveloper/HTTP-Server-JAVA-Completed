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
        ProcessedCommand processedCommand = CommandProcessor.extractRequest(command);
        if (processedCommand == null) {
            // null -> wrong command
            System.out.println("Wrong Command");
        } else {
            byte[] requestPacket = makeHttpPacket(processedCommand);
            requestSender.sendHttpPacket(requestPacket);
        }
    }

    private byte[] makeHttpPacket(ProcessedCommand processedCommand) {
        return RequestHeadAdder.addHeader(processedCommand.getMethod(), processedCommand.getUri(), hostName,
                processedCommand.getJsonBody());

    }
}
