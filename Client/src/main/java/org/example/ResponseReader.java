package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseReader {
    private final Socket socket;

    public ResponseMessage readResponse() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int packetLength = dataInputStream.readInt();
            byte[] httpResponsePacket = new byte[packetLength];
            dataInputStream.readFully(httpResponsePacket);

            String httpResponse = new String(httpResponsePacket);
            BufferedReader reader = new BufferedReader(new StringReader(httpResponse));

            // status Parsing;
            String line = reader.readLine();
            String[] statusLines = line.split(" ");
            String protocol = statusLines[0];
            String statusCode = statusLines[1];
            String statusPhrase = statusLines[2];

            // Header Parsing
            Map<String, String> headerMap = new HashMap<>();
            while (!(line = reader.readLine()).isEmpty()) {
                String[] header = line.split(" ");
                headerMap.put(header[0], header[1]);
            }

            // body Parsing
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

            return new ResponseMessage(protocol, statusCode, statusPhrase, headerMap, bodyMap);


        } catch (IOException e) {
            System.out.println("ResponseReader error: " + e.getMessage());
        }
        return null;
    }
}
