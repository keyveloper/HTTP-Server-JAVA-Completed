package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            System.out.println("Reading Request...[ \n");
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

            // read Request packet
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

            Request request = new Request();
            Map<String, String> headerMap = new HashMap<>();

            while (!(line = bufferedReader.readLine()).isEmpty()) {
                int pivot = line.indexOf(": ");
                if (pivot == -1) {
                    // read Request Line
                    String[] requestLine = line.split(" ");
                    String method = requestLine[0];
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

            if (headerMap.containsKey("content-length")) {
                System.out.println("content-length: " + headerMap.get("content-length"));
                byte[] bodyBytes = new byte[Integer.parseInt(headerMap.get("content-length"))];
                dataInputStream.readFully(bodyBytes);
                System.out.println("body: " + Arrays.toString(bodyBytes));

                request.setBodyBytes(bodyBytes);
            }

            return request;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
