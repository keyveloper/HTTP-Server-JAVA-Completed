package org.example;

import lombok.Data;

import java.io.DataOutputStream;
import java.io.IOException;

@Data
public class RequestSender {
    private final Client client;

    public void sendHttpRequest(byte[] httpPacket) {
        try {
            DataOutputStream dateOutputStream = new DataOutputStream(client.getSocket().getOutputStream());
            dateOutputStream.writeInt(httpPacket.length);
            dateOutputStream.write(httpPacket);
            dateOutputStream.flush();
        } catch (IOException e) {
            System.out.println("Request Sender error: " + e.getMessage());
        }
    }
}
