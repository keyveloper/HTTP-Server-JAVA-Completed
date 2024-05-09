package org.example;

import lombok.Data;

import java.io.DataOutputStream;
import java.io.IOException;

@Data
public class RequestSender {
    private final Client owner;

    public void divideRequest(Request request) {
        switch (request.getRequestCode()) {
            case GET_TIME -> requestTime();
        }
    }

    private void requestTime() {
        String request = "GET / HTTP1.1\r\n" +
                         "Host: " + owner.getHostName() + "\r\n";
        sendHttpPacket(request.getBytes());
    }

    private void sendHttpPacket(byte[] httpPacket) {
        try {
            DataOutputStream dateOutputStream = new DataOutputStream(owner.getSocket().getOutputStream());
            dateOutputStream.writeInt(httpPacket.length);
            dateOutputStream.write(httpPacket);
            dateOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
