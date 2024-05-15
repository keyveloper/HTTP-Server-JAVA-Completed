package org.example;


import lombok.Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

@Data
public class Server {
    private final int port = 8080;
    private final ServiceProcessor  serviceProcessor= new ServiceProcessor();

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, clientSocket, new RequestReader(this, clientSocket));
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("ServerIOException: " + e.getMessage());
        }
    }

    public void service(RequestMessage requestMessage) {
        byte[] resultBytes = serviceProcessor.service(requestMessage);

    }

}
