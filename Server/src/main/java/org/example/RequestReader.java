package org.example;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class RequestReader {
    private final Socket clientSocket;

    public Request readRequest() {
        // Read the request (optional, for logging or processing)
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            Map<String, String> headerMap = new HashMap<>();
            Request request = new Request();

            while (true) {
                line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }

                int pivot = line.indexOf(": ");
                if (pivot != -1) {
                    String headerKey = line.substring(0, pivot).trim().toLowerCase();
                    String headerValue = line.substring(pivot + 1).trim();
                    headerMap.put(headerKey, headerValue);
                    request.setHeaderMap(headerMap);
                } else {
                    String[] requestLine = line.split(" ");
                    request.setMethod(requestLine[0]);
                    request.setUri(requestLine[1]);
                    request.setProtocol(requestLine[2]);
                }
            }

            if (headerMap.containsKey("content-length")) {
                System.out.println(headerMap.get("content-length"));
                int contentLength = Integer.parseInt(headerMap.get("content-length"));
                char[] body = new char[contentLength];
                reader.read(body, 0, contentLength);
                System.out.println("Body: " + new String(body) + "\n");
                request.setBody(new String(body));
            }

            return request;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

