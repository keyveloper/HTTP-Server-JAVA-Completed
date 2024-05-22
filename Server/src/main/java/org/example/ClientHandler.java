package org.example;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class ClientHandler implements Runnable{
    private final Socket clientSocket;

    @Override
    public void run() {
        try {
            while (true) {
                Request request = readRequest();
                System.out.println("request: " + request);
                if (!isValidRequest(request)) {
                    clientSocket.close();
                    break;
                }
                response(request);
            }
        } catch (IOException e) {
            System.out.println("Client Handler Error: " + e.getMessage());
        }
    }

    private boolean isValidRequest(Request request) {
        return Objects.equals(request.getProtocol(), "HTTP/1.1") && request.getMethod() !=
                null && request.getUri() != null;
    }

    private void response(Request request) {
        RequestHandler requestHandler =  new RequestHandler();
        Response response = requestHandler.handleRequest(request);
        sendHttpResponse(response);
    }

    public void sendHttpResponse(Response response) {
        ResponseSender responseSender = new ResponseSender(clientSocket);
        // send ResponsePacket with http header
        responseSender.sendHttpResponse(HttpResponseMaker.makePacket(response));
    }

    private Request readRequest() {
        try {
            System.out.println("\n[[[client]]] : " + clientSocket + "\n");
            System.out.println("Reading Request...\n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            Request request = new Request();
            Map<String, String> headerMap = new HashMap<>();
            boolean isHeaders = true;
            int contentLength = 0;

            // Read the request line
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                System.out.println("line : " + line + "socket: " + clientSocket);
                int pivot = line.indexOf(": ");
                if (pivot == -1) {
                    String[] requestElements = line.split(" ");
                    String method = requestElements[0];
                    String uri = requestElements[1];
                    String protocol = requestElements[2];

                    request.setMethod(method);
                    request.setUri(uri);
                    request.setProtocol(protocol);
                    System.out.println("request: " + request);
                } else {
                    String headerKey = line.substring(0, pivot).trim().toLowerCase();
                    String headerValue = line.substring(pivot).trim();
                    headerMap.put(headerKey, headerValue);
                }
            }

            request.setHeaderMap(headerMap);

            for (String key : headerMap.keySet()) {
                System.out.println(key + ": " + headerMap.get(key));
            }

            if (headerMap.containsKey("content-length")) {
                contentLength = Integer.parseInt(headerMap.get("content-length"));
            }

            char[] body = new char[contentLength];
            if (contentLength > 0 && bufferedReader.read(body) > 0) {
                System.out.println("Body: \n" + new String(body));
            }

            request.setClientSocket(clientSocket);
            return request;

        } catch (IOException e) {
            System.out.println("Request Reader Error: " + e.getMessage());
            return null;
        }
    }
}
