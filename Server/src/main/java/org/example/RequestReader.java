package org.example;

import lombok.AllArgsConstructor;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RequestReader {
    private final Socket clientSocket;

    public Request readRequest() {
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
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("<<read line>>\n" + line);
                if (line.isEmpty() && isHeaders) {
                    isHeaders = false;
                }

                if (isHeaders) {
                    System.out.println("parsing header!!");
                    int pivot = line.indexOf(": ");
                    if (pivot == -1) {
                        System.out.println("is request Line!!");
                        String[] requestLines = line.split(" ");
                        String method = requestLines[0];
                        String uri = requestLines[1];
                        String protocol = requestLines[2];

                        System.out.println(method + uri + protocol);

                        request.setMethod(method);
                        request.setUri(uri);
                        request.setProtocol(protocol);
                    } else {
                        String headerKey = line.substring(0, pivot).trim().toLowerCase();
                        String headerValue = line.substring(pivot + 1).trim();
                        headerMap.put(headerKey, headerValue);
                    }
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

            System.out.println("<<<<request: >>>>>" + request);
            return request;

        } catch (IOException e) {
            System.out.println("Request Reader Error: " + e.getMessage());
            return null;
        }
    }
}