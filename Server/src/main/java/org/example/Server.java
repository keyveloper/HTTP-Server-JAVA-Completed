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

    public void start() {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());

                    // Read the request (optional, for logging or processing)
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String line;
                    Map<String, String> headerMap = new HashMap<>();
                    while (true) {
                        line = reader.readLine();
                        if (line == null || line.isEmpty()) {
                            break;
                        }
                        System.out.println(line);
                        if (line.contains(":")) {
                            String[] header = line.split(":");
                            headerMap.put(header[0].trim().toLowerCase(), header[1].trim());
                        }
                    }

                    if (headerMap.containsKey("content-length")) {
                        int contentLength = Integer.parseInt(headerMap.get("content-length"));
                        char[] body = new char[contentLength];
                        reader.read(body, 0, contentLength);
                        System.out.println("Body: " + new String(body));
                    }

                    // Write a response
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html; charset=utf-8");
                    out.println();
                    out.println("<html><body><h1>Hello, World!</h1></body></html>");

                    out.close();
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
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