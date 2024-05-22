package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

@AllArgsConstructor
public class ResponseSender {
    private final Socket clientSocket;
    public void sendHttpResponse(byte[] httpResponsePacket) {
        System.out.println("[Request Sender]\n send ResponsePacket: "+ Arrays.toString(httpResponsePacket));
        try {
            DataOutputStream dateOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dateOutputStream.write(httpResponsePacket);
            dateOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
