package org.example;


import lombok.Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

@Data
public class Server {
    private final int port = 8080;
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("[Waiting Client,,,]");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected " + clientSocket.getLocalPort() + " Port, From " +
                        clientSocket.getRemoteSocketAddress().toString() + "\n");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("ServerIOException: " + e.getMessage());
        }
    }
}
