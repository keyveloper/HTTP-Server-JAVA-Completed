package org.example;

import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

@Data
public class ClientHandler implements Runnable{
    private final Server server;
    private final Socket client;
    private final RequestReader requestReader = new RequestReader(server, client);
    private final ResponseSender responseSender = new ResponseSender(client);
    @Override
    public void run() {
        while (true) {
            RequestMessage requestMessage = requestReader.readRequest();
            server.service(requestMessage);
        }
    }
}
