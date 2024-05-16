package org.example;

import lombok.Data;

import java.net.Socket;

@Data
public class Client{
    private final Socket socket = new Socket();
    private final String hostName;
    private CommandProcessor commandProcessor = new CommandProcessor(this);

    public void run() {
        while (true) {
            ResponseReader responseReader = new ResponseReader(socket);

        }
    }

    public void processCommand(String command) {
        RequestSender requestSender = new RequestSender(this);
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
