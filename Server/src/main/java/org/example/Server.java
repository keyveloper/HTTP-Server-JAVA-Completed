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


    public Request readRequest(Socket clientSocket) {
        try {
            System.out.println("Reading Request...[ \n");
            InputStream inputStream = clientSocket.getInputStream();
            // read Request packet
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            Request request = new Request();
            Map<String, String> headerMap = new HashMap<>();
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                System.out.println("line: " + line);
                System.out.println("noooo");
                int pivot = line.indexOf(": ");
                if (pivot == -1) {
                    // read Request Line
                    String[] requestLine = line.split(" ");
                    String method = requestLine[0].toUpperCase();
                    String uri = requestLine[1];
                    String protocol = requestLine[2];

                    request.setMethod(method);
                    request.setUri(uri);
                    request.setProtocol(protocol);
                } else {
                    String fieldKey = line.substring(0, pivot).trim().toLowerCase();
                    String fieldValue = line.substring(pivot + 1).trim();
                    headerMap.put(fieldKey, fieldValue);
                }
            }
            request.setHeaderMap(headerMap);

            return request;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}