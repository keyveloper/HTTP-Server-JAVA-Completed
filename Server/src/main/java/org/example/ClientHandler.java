package org.example;

import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

@Data
public class ClientHandler implements Runnable{
    private final Server owner;
    private final Socket client;
    private final RequestReader requestReader;
    @Override
    public void run() {
        while (true) {
            RequestMessage requestMessage = requestReader.readRequest();
            owner.service(requestMessage);
        }
    }
}
