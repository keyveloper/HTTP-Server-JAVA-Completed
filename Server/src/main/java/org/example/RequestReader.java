package org.example;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class RequestReader {
    private final Socket clientSocket;

    public Request readRequest() {
        // Read the request (optional, for logging or processing)
        try {
            InputStream inputStream = clientSocket.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] httpRequestBytes = buffer.toByteArray();

            String httpRequest = new String(httpRequestBytes, StandardCharsets.UTF_8);
            int blankLineIndex = httpRequest.indexOf("\r\n\r\n");
            if (blankLineIndex == -1) {
                throw new IOException("Malformed HTTP request: No header-body separator found.");
            }

            String header = httpRequest.substring(0, blankLineIndex);
            String body = httpRequest.substring(blankLineIndex + 2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

