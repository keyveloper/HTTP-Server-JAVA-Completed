package org.example;


import lombok.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Data
public class Server {
    private final int port = 8080;
    private final RequestHandler requestHandler = new RequestHandler();

    public void start() {
        int port = 8080;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("[waiting Client]");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, requestHandler);
                Thread handlerThread = new Thread(clientHandler);
                handlerThread.start();
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port " + port);
            System.out.println("Exception: " + e.getMessage());
        }
    }
}