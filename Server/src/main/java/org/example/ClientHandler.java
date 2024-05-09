package org.example;

import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

@Data
public class ClientHandler implements Runnable{
    private final Socket client;
    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream dataInputStream = new DataInputStream(client.getInputStream());


            } catch (IOException e) {
                System.out.println("ClientHandlerIOException: " + e.getMessage());
            }
        }
    }
}
