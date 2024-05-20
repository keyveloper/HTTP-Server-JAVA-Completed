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
    public RequestMessage readRequest() {
        try {
            System.out.println("Reading Request...[ \n");
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

            int packetLength = dataInputStream.readInt();
            byte[] httpPacket = new byte[packetLength];
            dataInputStream.readFully(httpPacket);
            System.out.println("read packet Length: " + packetLength + "\n");
            System.out.println("httpPacket: " + Arrays.toString(httpPacket) + "\n");

            String httpRequest = new String(httpPacket);
            System.out.println("http Packet to String: \n" + httpRequest + "\n");

            System.out.println("start to read request line : {\n" );
            BufferedReader reader = new BufferedReader(new StringReader(httpRequest));
            // Request Parsing
            String line = reader.readLine();
            System.out.println("read request line: " + line + "\n");
            String[] requestLine = line.split(" ");
            String method = requestLine[0];
            String uri = requestLine[1];
            String httpVersion = requestLine[2];
            System.out.println("requestLine: " + Arrays.toString(requestLine) + "\b");
            System.out.println("method, uri, httpVersion: " + method + uri + httpVersion + "\n");
            System.out.println("}\n all request line read!!\n");

            // Header Parsing
            System.out.println("start to read header: {\n" );
            Map<String, String> headerMap = new HashMap<>();
            while (!((line = reader.readLine()) == null)) {
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
                System.out.println("Key: " + key + ", Value: " + headerMap.get(key));
            }

            // Body Parsing
            System.out.println("start to read body: {\n" );
            StringBuilder body = new StringBuilder();
            if (headerMap.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
                char[] bodyChars = new char[contentLength];
                int charsRead = reader.read(bodyChars, 0, contentLength);
                body.append(bodyChars, 0, charsRead);
            }

            System.out.println("body: " + body);
            if (!body.toString().isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                // add Object -> Json logics
            } else {
                System.out.println("received Empty body!");
            }

            System.out.println("]return request!! \n");

            return  new RequestMessage(method, uri, httpVersion, headerMap, null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
