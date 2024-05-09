package org.example;

import lombok.Data;

import java.io.*;
import java.net.Socket;

@Data
public class RequestReader {
    private final Server owner;
    private final Socket clientSocket;
    public void readRequest() {
        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            while (true) {

                System.out.println("readRequest: " + line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readLine(DataInputStream dataInputStream) {
        StringBuilder line = new StringBuilder();
        char c;
        boolean rFlag = false;

        while (true) {
            try {
                byte readByte = dataInputStream.readByte();
                if (readByte == -1) {
                    return null;
                }

                c = (char) readByte;

                if (rFlag && c == '\n') {
                    break;
                }

                rFlag = (c == '\r');

                if (!rFlag) {
                    line.append(c);
                }
            } catch (IOException e) {
                System.out.println("In readRequest readLine IOException: " + e.getMessage());
            }
        }

        return line.toString();

    }
}
