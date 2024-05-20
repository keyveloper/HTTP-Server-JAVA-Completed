package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class ResponseReader implements Runnable {
    private final Client client;

    @Override
    public void run() {
        ResponseMessage responseMessage = readResponse();
        client.handleResponse(responseMessage);
    }
    public ResponseMessage readResponse() {
        try {
            System.out.println("Reading Response...[ \n");
            DataInputStream dataInputStream = new DataInputStream(client.getSocket().getInputStream());
            int packetLength = dataInputStream.readInt();
            byte[] httpResponsePacket = new byte[packetLength];
            dataInputStream.readFully(httpResponsePacket);
            System.out.println("read packet Length: " + packetLength + "\n");
            System.out.println("httpPacket: " + Arrays.toString(httpResponsePacket) + "\n");


            String httpResponse = new String(httpResponsePacket);
            System.out.println("http Packet to String: \n" + httpResponse + "\n");

            System.out.println("start to read request line : {\n" );
            BufferedReader reader = new BufferedReader(new StringReader(httpResponse));

            // status Parsing;
            String line = reader.readLine();
            System.out.println("read status line: " + line + "\n");
            String[] statusLines = line.split(" ");
            String protocol = statusLines[0];
            String statusCode = statusLines[1];
            String statusPhrase = statusLines[2];
            System.out.println("requestLine: " + Arrays.toString(statusLines) + "\b");
            System.out.println("protocol, statusCode, statusPhrase: " + protocol + statusCode + statusPhrase + "\n");
            System.out.println("}\n all response line read!!\n");

            // Header Parsing
            System.out.println("start to read header: {\n" );
            Map<String, String> headerMap = new HashMap<>();
            while (!((line = reader.readLine()) == null) && !line.isEmpty()) {
                System.out.println(" read line: " + line + "\n");
                int idx = line.indexOf(": ");
                if (idx != -1) {
                    String key = line.substring(0, idx).trim();
                    String value = line.substring(idx + 1).trim();
                    headerMap.put(key, value);
                }
            }
            System.out.println("}\n all header read! \n");

            for (String key : headerMap.keySet()) {
                System.out.println(key + " " + headerMap.get(key));
            }

            // body Parsing
            System.out.println("start to read body: {\n" );
            StringBuilder body = new StringBuilder();
            ObjectMapper objectMapper = new ObjectMapper();
            while ((line = reader.readLine()) != null)  {
                body.append(line);
//                if (headerMap.containsKey("Content-Length")) {
//                    int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
//                    System.out.println("contentLength: " + contentLength);
//                    char[] bodyChars = new char[contentLength];
//                    int charsRead, remainingLength = contentLength;
//
//                    while (remainingLength > 0 && (charsRead = reader.read(bodyChars, contentLength - remainingLength, remainingLength)) != -1) {
//                        body.append(bodyChars, contentLength - remainingLength, charsRead);
//                        remainingLength -= charsRead;
//                    }
//                }
            }


            Map<String, Object> bodyMap = new HashMap<>();
            if (!body.isEmpty()) {
                bodyMap = objectMapper.readValue(body.toString(), new TypeReference<Map<String, Object>>() {
                });
                for (String key : bodyMap.keySet()) {
                    System.out.println(key + " " + bodyMap.get(key));
                }
            }

            System.out.println("]return request!! \n");

            return new ResponseMessage(protocol, statusCode, statusPhrase, headerMap, bodyMap);

        } catch (IOException e) {
            System.out.println("ResponseReader error: " + e.getMessage());
        }
        return null;
    }
}
