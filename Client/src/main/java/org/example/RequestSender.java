package org.example;

import lombok.Data;

import java.io.DataOutputStream;
import java.io.IOException;

@Data
public class RequestSender {
    private final Client owner;

    public void sendHttpRequest(byte[] httpPacket) {
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
