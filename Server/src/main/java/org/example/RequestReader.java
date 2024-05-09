package org.example;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Data
public class RequestReader {
    private final Server owner;
    private final Socket clientSocket;
    public void readRequest() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String line = reader.readLine();
                System.out.println("readRequest: " + line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
