package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RequestReader {
    private final Socket clientSocket;
    public RequestMessage readRequest() {
        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            int packetLength = dataInputStream.readInt();
            byte[] httpPacket = new byte[packetLength];
            dataInputStream.readFully(httpPacket);

            String httpRequest = new String(httpPacket);
            BufferedReader reader = new BufferedReader(new StringReader(httpRequest));

            // Request Parsing
            String line = reader.readLine();
            String[] requestLine = line.split(" ");
            String method = requestLine[0];
            String uri = requestLine[1];
            String httpVersion = requestLine[2];


            // Header Parsing
            Map<String, String> headerMap = new HashMap<>();
            while (!(line = reader.readLine()).isEmpty()) {
                String[] header = line.split(" ");
                headerMap.put(header[0], header[1]);
            }

            // Body Parsing
            StringBuilder body = new StringBuilder();
            ObjectMapper objectMapper = new ObjectMapper();
            if (headerMap.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
                char[] bodyChars = new char[contentLength];
                int charsRead = reader.read(bodyChars, 0, contentLength);
                reader.read(bodyChars, 0, contentLength);
                body.append(bodyChars, 0, charsRead);
            }
            Map<String, Object> bodyMap = objectMapper.readValue(body.toString(), Map.class);

            return  new RequestMessage(method, uri, httpVersion, headerMap, bodyMap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
