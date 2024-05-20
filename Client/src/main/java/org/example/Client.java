package org.example;

import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

@Data
public class Client{
    private final Socket socket = new Socket();
    private final String hostName;
    private CommandProcessor commandProcessor = new CommandProcessor(this);
    private final int tcpClientPort = 8080;

    public void run() {
        System.out.println("\n[ Request ... ]");
        try {
            socket.connect(new InetSocketAddress("localhost", tcpClientPort));
            System.out.print("\n[ Success connecting ] \n");
            Thread reponseReaderThread = new Thread(new ResponseReader(this));
            reponseReaderThread.start();

        } catch (IOException e) {
            System.out.println("Client connect Error: " + e.getMessage());
        }
    }

    public void processCommand(String command) {
        RequestSender requestSender = new RequestSender(this);
        Request request = commandProcessor.extractRequest(command);
        System.out.println("extract request : " + request);
        if (request == null) {
            // null -> wrong command
            System.out.println("Wrong Command");
        } else {
            byte[] requestPacket = HttpRequestMaker.makeHttpRequest(request);
            System.out.println("request Packet is made:" + Arrays.toString(requestPacket) + "\n");
            requestSender.sendHttpRequest(requestPacket);
        }
    }

    public void handleResponse(ResponseMessage responseMessage) {
        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.handleResponse(responseMessage);
    }

}
